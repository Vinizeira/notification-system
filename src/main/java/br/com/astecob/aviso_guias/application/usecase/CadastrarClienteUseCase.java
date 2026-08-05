package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import br.com.astecob.aviso_guias.exception.ClienteDuplicadoException;
import org.springframework.stereotype.Service;

@Service
public class CadastrarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public CadastrarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente executar(
            String nomeEmpresa,
            String email,
            String telefoneWhatsapp
    ) {
        Cliente cliente = Cliente.novo(
                nomeEmpresa,
                email,
                telefoneWhatsapp
        );

        boolean clienteJaExiste = clienteRepository
                .existePorNomeEmpresaNormalizado(
                        cliente.getNomeEmpresaNormalizado()
                );

        if (clienteJaExiste) {
            throw new ClienteDuplicadoException(
                    cliente.getNomeEmpresa()
            );
        }

        return clienteRepository.salvar(cliente);
    }
}