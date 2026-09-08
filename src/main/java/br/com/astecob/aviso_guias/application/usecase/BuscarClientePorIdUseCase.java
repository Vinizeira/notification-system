package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import br.com.astecob.aviso_guias.exception.ClienteNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarClientePorIdUseCase {

    private final ClienteRepository clienteRepository;

    public BuscarClientePorIdUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente executar(UUID id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }
}