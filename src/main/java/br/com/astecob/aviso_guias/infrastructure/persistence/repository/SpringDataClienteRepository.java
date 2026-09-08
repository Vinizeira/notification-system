package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataClienteRepository
        extends JpaRepository<ClienteEntity, UUID> {

    Optional<ClienteEntity> findByNomeEmpresaNormalizado(
            String nomeEmpresaNormalizado
    );

    boolean existsByNomeEmpresaNormalizado(
            String nomeEmpresaNormalizado
    );
}