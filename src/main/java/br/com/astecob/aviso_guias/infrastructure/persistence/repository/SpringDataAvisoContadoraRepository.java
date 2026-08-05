package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.infrastructure.persistence.entity.AvisoContadoraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataAvisoContadoraRepository extends JpaRepository<AvisoContadoraEntity, UUID> {
}