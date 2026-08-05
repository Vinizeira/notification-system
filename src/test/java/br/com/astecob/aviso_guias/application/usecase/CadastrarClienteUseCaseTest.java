package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import br.com.astecob.aviso_guias.exception.ClienteDuplicadoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarClienteUseCaseTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CadastrarClienteUseCase cadastrarClienteUseCase;

    @Test
    void deveCadastrarClienteQuandoEmpresaAindaNaoExiste() {
        when(clienteRepository.existePorNomeEmpresaNormalizado("ASTECOB"))
                .thenReturn(false);

        when(clienteRepository.salvar(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cliente cliente = cadastrarClienteUseCase.executar(
                "Astecob",
                "contato@astecob.com",
                "11999999999"
        );

        assertEquals("Astecob", cliente.getNomeEmpresa());
        assertEquals("ASTECOB", cliente.getNomeEmpresaNormalizado());

        verify(clienteRepository)
                .existePorNomeEmpresaNormalizado("ASTECOB");

        verify(clienteRepository).salvar(any(Cliente.class));
    }

    @Test
    void deveRejeitarCadastroQuandoEmpresaJaExiste() {
        when(clienteRepository.existePorNomeEmpresaNormalizado("ASTECOB"))
                .thenReturn(true);

        assertThrows(
                ClienteDuplicadoException.class,
                () -> cadastrarClienteUseCase.executar(
                        "Astecob",
                        "contato@astecob.com",
                        "11999999999"
                )
        );

        verify(clienteRepository, never()).salvar(any(Cliente.class));
    }
}