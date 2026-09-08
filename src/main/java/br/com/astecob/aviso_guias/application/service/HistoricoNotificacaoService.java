package br.com.astecob.aviso_guias.application.service;

import br.com.astecob.aviso_guias.domain.enums.CanalNotificacao;
import br.com.astecob.aviso_guias.domain.enums.ResultadoNotificacao;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.model.HistoricoNotificacao;
import br.com.astecob.aviso_guias.domain.repository.HistoricoNotificacaoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HistoricoNotificacaoService {

    private final HistoricoNotificacaoRepository historicoNotificacaoRepository;

    public HistoricoNotificacaoService(HistoricoNotificacaoRepository historicoNotificacaoRepository) {
        this.historicoNotificacaoRepository = historicoNotificacaoRepository;
    }

    public void registrarSucesso(UUID guiaId, Cliente cliente, CanalNotificacao canal) {
        HistoricoNotificacao historico = new HistoricoNotificacao(
                guiaId,
                cliente.getNomeEmpresa(),
                cliente.getEmail(),
                cliente.getTelefoneWhatsapp(),
                canal,
                ResultadoNotificacao.SUCESSO,
                null
        );
        historicoNotificacaoRepository.salvar(historico);
    }

    public void registrarFalha(UUID guiaId, Cliente cliente, CanalNotificacao canal, String motivo) {
        HistoricoNotificacao historico = new HistoricoNotificacao(
                guiaId,
                cliente != null ? cliente.getNomeEmpresa() : null,
                cliente != null ? cliente.getEmail() : null,
                cliente != null ? cliente.getTelefoneWhatsapp() : null,
                canal,
                ResultadoNotificacao.FALHA,
                motivo
        );
        historicoNotificacaoRepository.salvar(historico);
    }
}
