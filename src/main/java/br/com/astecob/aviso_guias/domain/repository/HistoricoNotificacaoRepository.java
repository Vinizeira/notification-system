package br.com.astecob.aviso_guias.domain.repository;

import br.com.astecob.aviso_guias.domain.model.HistoricoNotificacao;
import java.util.List;
import java.util.UUID;

public interface HistoricoNotificacaoRepository {
    HistoricoNotificacao salvar(HistoricoNotificacao historico);
    List<HistoricoNotificacao> buscarPorGuiaId(UUID guiaId);
}