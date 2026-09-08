package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.application.port.EmailSender;
import br.com.astecob.aviso_guias.domain.enums.ResultadoNotificacao;
import br.com.astecob.aviso_guias.domain.enums.StatusGuia;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.model.Guia;
import br.com.astecob.aviso_guias.domain.model.HistoricoNotificacao;
import br.com.astecob.aviso_guias.domain.repository.HistoricoNotificacaoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReenviarNotificacaoUseCaseTest {
    private final EmailSender sender = mock(EmailSender.class);
    private final HistoricoNotificacaoRepository repository = mock(HistoricoNotificacaoRepository.class);
    private final ReenviarNotificacaoUseCase useCase = new ReenviarNotificacaoUseCase(sender, repository);
    private final Cliente cliente = Cliente.novo("Empresa Exemplo", "contato@example.com", "11999999999");
    private final Guia guia = new Guia("FGTS", "EMPRESA EXEMPLO", 6, 2026,
            LocalDate.of(2026, 6, 20), cliente.getId(), StatusGuia.FALHA_EMAIL);

    @Test
    void deveRegistrarFalhaEInformarErroAoChamador() {
        when(sender.enviar(any(), any(), any(), any())).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> useCase.executar(guia, cliente, new File("guia.pdf")));

        ArgumentCaptor<HistoricoNotificacao> captor = ArgumentCaptor.forClass(HistoricoNotificacao.class);
        verify(repository).salvar(captor.capture());
        assertEquals(ResultadoNotificacao.FALHA, captor.getValue().getResultado());
        assertNotNull(captor.getValue().getMotivoFalha());
    }

    @Test
    void deveRegistrarSucessoSemLancarErro() {
        when(sender.enviar(any(), any(), any(), any())).thenReturn(true);

        assertDoesNotThrow(() -> useCase.executar(guia, cliente, new File("guia.pdf")));

        ArgumentCaptor<HistoricoNotificacao> captor = ArgumentCaptor.forClass(HistoricoNotificacao.class);
        verify(repository).salvar(captor.capture());
        assertEquals(ResultadoNotificacao.SUCESSO, captor.getValue().getResultado());
        assertNull(captor.getValue().getMotivoFalha());
    }
}
