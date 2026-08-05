package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarClientesUseCaseTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ListarClientesUseCase listarClientesUseCase;

    @Test
    void deveListarClientesCadastrados() {
        List<Cliente> clientes = List.of(
                Cliente.novo("Astecob", "contato@astecob.com", "11999999999"),
                Cliente.novo("Empresa Dois", "contato@empresa2.com", "11888888888")
        );

        when(clienteRepository.listarTodos()).thenReturn(clientes);

        List<Cliente> resultado = listarClientesUseCase.executar();

        assertEquals(clientes, resultado);
        verify(clienteRepository).listarTodos();
    }
}