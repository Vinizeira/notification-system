package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import br.com.astecob.aviso_guias.exception.ClienteNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AtualizarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public AtualizarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente executar(UUID id, String nomeEmpresa, String email, String telefoneWhatsapp) {
        Cliente clienteExistente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        Cliente clienteAtualizado = Cliente.restaurar(
                clienteExistente.getId(),
                nomeEmpresa,
                email,
                telefoneWhatsapp,
                clienteExistente.getCriadoEm()
        );

        return clienteRepository.salvar(clienteAtualizado);
    }
}