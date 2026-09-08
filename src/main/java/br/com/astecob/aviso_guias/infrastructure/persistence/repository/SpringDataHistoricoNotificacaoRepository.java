package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.infrastructure.persistence.entity.HistoricoNotificacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataHistoricoNotificacaoRepository extends JpaRepository<HistoricoNotificacaoEntity, UUID> {
    List<HistoricoNotificacaoEntity> findByGuiaId(UUID guiaId);
}