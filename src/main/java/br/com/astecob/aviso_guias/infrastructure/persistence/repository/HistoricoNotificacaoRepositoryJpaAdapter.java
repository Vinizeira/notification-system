package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.enums.CanalNotificacao;
import br.com.astecob.aviso_guias.domain.enums.ResultadoNotificacao;
import br.com.astecob.aviso_guias.domain.model.HistoricoNotificacao;
import br.com.astecob.aviso_guias.domain.repository.HistoricoNotificacaoRepository;
import br.com.astecob.aviso_guias.infrastructure.persistence.entity.HistoricoNotificacaoEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class HistoricoNotificacaoRepositoryJpaAdapter implements HistoricoNotificacaoRepository {

    private final SpringDataHistoricoNotificacaoRepository springDataRepository;

    public HistoricoNotificacaoRepositoryJpaAdapter(SpringDataHistoricoNotificacaoRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public HistoricoNotificacao salvar(HistoricoNotificacao historico) {
        HistoricoNotificacaoEntity salva = springDataRepository.save(toEntity(historico));
        return toDomain(salva);
    }

    @Override
    public List<HistoricoNotificacao> buscarPorGuiaId(UUID guiaId) {
        return springDataRepository.findByGuiaId(guiaId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private HistoricoNotificacaoEntity toEntity(HistoricoNotificacao historico) {
        return new HistoricoNotificacaoEntity(
                historico.getId(), historico.getGuiaId(), historico.getCanal().name(),
                historico.getResultado().name(), historico.getDataHora(), historico.getMotivoFalha()
        );
    }

    private HistoricoNotificacao toDomain(HistoricoNotificacaoEntity entity) {
        return HistoricoNotificacao.restaurar(
                entity.getId(), entity.getGuiaId(), CanalNotificacao.valueOf(entity.getCanal()),
                ResultadoNotificacao.valueOf(entity.getResultado()), entity.getDataHora(), entity.getMotivoFalha()
        );
    }
}