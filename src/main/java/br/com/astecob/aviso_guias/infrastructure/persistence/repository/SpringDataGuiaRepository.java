package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.infrastructure.persistence.entity.GuiaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataGuiaRepository extends JpaRepository<GuiaEntity, UUID> {
    Optional<GuiaEntity> findByTipoGuiaAndNomeEmpresaNormalizadoAndMesAndAno(
            String tipoGuia, String nomeEmpresaNormalizado, Integer mes, Integer ano);

    @Transactional
    @Modifying
    @Query("UPDATE GuiaEntity g SET g.clienteId = null WHERE g.clienteId = :clienteId")
    void desvincularCliente(UUID clienteId);
}