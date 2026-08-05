package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import br.com.astecob.aviso_guias.exception.ClienteNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarClientePorIdUseCaseTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    @Test
    void deveBuscarClienteQuandoIdExiste() {
        Cliente cliente = Cliente.novo(
                "Astecob",
                "contato@astecob.com",
                "11999999999"
        );

        when(clienteRepository.buscarPorId(cliente.getId()))
                .thenReturn(Optional.of(cliente));

        Cliente encontrado = buscarClientePorIdUseCase.executar(cliente.getId());

        assertEquals(cliente, encontrado);
        verify(clienteRepository).buscarPorId(cliente.getId());
    }

    @Test
    void deveRejeitarBuscaQuandoClienteNaoExiste() {
        UUID id = UUID.randomUUID();

        when(clienteRepository.buscarPorId(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ClienteNaoEncontradoException.class,
                () -> buscarClientePorIdUseCase.executar(id)
        );

        verify(clienteRepository).buscarPorId(id);
    }
}