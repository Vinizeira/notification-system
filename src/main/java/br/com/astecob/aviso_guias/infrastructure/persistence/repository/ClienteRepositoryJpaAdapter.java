package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import br.com.astecob.aviso_guias.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

import java.util.Optional;

@Repository
public class ClienteRepositoryJpaAdapter implements ClienteRepository {

    private final SpringDataClienteRepository springDataClienteRepository;

    public ClienteRepositoryJpaAdapter(
            SpringDataClienteRepository springDataClienteRepository
    ) {
        this.springDataClienteRepository = springDataClienteRepository;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteEntity entity = ClienteEntity.fromDomain(cliente);

        ClienteEntity entitySalva = springDataClienteRepository.save(entity);

        return entitySalva.toDomain();
    }

    @Override
    public Optional<Cliente> buscarPorNomeEmpresaNormalizado(
            String nomeEmpresaNormalizado
    ) {
        return springDataClienteRepository
                .findByNomeEmpresaNormalizado(nomeEmpresaNormalizado)
                .map(ClienteEntity::toDomain);
    }

    @Override
    public boolean existePorNomeEmpresaNormalizado(
            String nomeEmpresaNormalizado
    ) {
        return springDataClienteRepository
                .existsByNomeEmpresaNormalizado(nomeEmpresaNormalizado);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return springDataClienteRepository.findById(id)
                .map(ClienteEntity::toDomain);
    }

    @Override
    public List<Cliente> listarTodos() {
        return springDataClienteRepository.findAll().stream()
                .map(ClienteEntity::toDomain)
                .toList();
    }
}