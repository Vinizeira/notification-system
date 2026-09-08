package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.application.service.NotificacaoOrchestrator;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.model.Guia;
import br.com.astecob.aviso_guias.domain.model.GuiaExtraida;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import br.com.astecob.aviso_guias.domain.repository.GuiaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessarGuiaUseCaseTest {

    @Mock
    private GuiaRepository guiaRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private NotificacaoOrchestrator notificacaoOrchestrator;
    @Mock
    private AvisarContadoraUseCase avisarContadoraUseCase;

    private ProcessarGuiaUseCase useCase;

    @Test
    void deveIgnorarGuiaJaProcessada() {
        useCase = new ProcessarGuiaUseCase(guiaRepository, clienteRepository, notificacaoOrchestrator, avisarContadoraUseCase);

        GuiaExtraida guiaExtraida = new GuiaExtraida("FGTS", "Empresa Exemplo", 6, 2026,
                Path.of("FGTS-Empresa Exemplo-06-2026.pdf"), LocalDate.of(2026, 6, 20));

        when(guiaRepository.buscarPorChaveNatural(any(), any(), any(), any()))
                .thenReturn(Optional.of(mock(Guia.class)));

        useCase.processar(guiaExtraida);

        verify(clienteRepository, never()).buscarPorNomeEmpresaNormalizado(any());
        verify(guiaRepository, never()).salvar(any());
    }

    @Test
    void deveCriarPendenciaQuandoClienteNaoEncontrado() {
        useCase = new ProcessarGuiaUseCase(guiaRepository, clienteRepository, notificacaoOrchestrator, avisarContadoraUseCase);

        GuiaExtraida guiaExtraida = new GuiaExtraida("DAS", "Empresa Nova", 7, 2026,
                Path.of("DAS-Empresa Nova-07-2026.pdf"), LocalDate.of(2026, 7, 20));

        when(guiaRepository.buscarPorChaveNatural(any(), any(), any(), any())).thenReturn(Optional.empty());
        when(clienteRepository.buscarPorNomeEmpresaNormalizado(any())).thenReturn(Optional.empty());

        useCase.processar(guiaExtraida);

        verify(guiaRepository, times(1)).salvar(any());
        verify(avisarContadoraUseCase, times(1)).avisarClienteNaoEncontrado(
                eq("DAS"), eq("Empresa Nova"), eq(7), eq(2026));
        verify(notificacaoOrchestrator, never()).processar(any(), any(), any());
    }

    @Test
    void deveOrquestrarNotificacaoQuandoClienteEncontrado() {
        useCase = new ProcessarGuiaUseCase(guiaRepository, clienteRepository, notificacaoOrchestrator, avisarContadoraUseCase);

        GuiaExtraida guiaExtraida = new GuiaExtraida("FGTS", "Empresa Exemplo", 6, 2026,
                Path.of("FGTS-Empresa Exemplo-06-2026.pdf"), LocalDate.of(2026, 6, 20));
        Cliente cliente = Cliente.novo("Empresa Exemplo", "contato@example.com", "5521999999999");

        when(guiaRepository.buscarPorChaveNatural(any(), any(), any(), any())).thenReturn(Optional.empty());
        when(clienteRepository.buscarPorNomeEmpresaNormalizado(any())).thenReturn(Optional.of(cliente));

        useCase.processar(guiaExtraida);

        verify(guiaRepository, times(2)).salvar(any());
        verify(notificacaoOrchestrator, times(1)).processar(any(), eq(cliente), any());
        verify(avisarContadoraUseCase, never()).avisarClienteNaoEncontrado(any(), any(), any(), any());
    }
}