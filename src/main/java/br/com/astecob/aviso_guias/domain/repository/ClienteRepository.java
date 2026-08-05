package br.com.astecob.aviso_guias.domain.repository;

import br.com.astecob.aviso_guias.domain.model.Cliente;
import java.util.List;
import java.util.UUID;

import java.util.Optional;

public interface ClienteRepository {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorNomeEmpresaNormalizado(
            String nomeEmpresaNormalizado
    );

    boolean existePorNomeEmpresaNormalizado(
            String nomeEmpresaNormalizado
    );

    Optional<Cliente> buscarPorId(UUID id);

    List<Cliente> listarTodos();
}