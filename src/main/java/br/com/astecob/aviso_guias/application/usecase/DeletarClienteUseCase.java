package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeletarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public DeletarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void executar(UUID id) {
        clienteRepository.deletarPorId(id);
    }
}