package br.com.astecob.aviso_guias.domain.model;

import br.com.astecob.aviso_guias.domain.enums.CanalNotificacao;
import br.com.astecob.aviso_guias.domain.enums.ResultadoNotificacao;

import java.time.Instant;
import java.util.UUID;

public class HistoricoNotificacao {

    private final UUID id;
    private final UUID guiaId;
    private final CanalNotificacao canal;
    private final ResultadoNotificacao resultado;
    private final Instant dataHora;
    private final String motivoFalha;

    private HistoricoNotificacao(UUID id, UUID guiaId, CanalNotificacao canal, ResultadoNotificacao resultado,
                                 Instant dataHora, String motivoFalha) {
        validar(guiaId, canal, resultado, motivoFalha);
        this.id = id;
        this.guiaId = guiaId;
        this.canal = canal;
        this.resultado = resultado;
        this.motivoFalha = motivoFalha;
        this.dataHora = dataHora;
    }

    public HistoricoNotificacao(UUID guiaId, CanalNotificacao canal, ResultadoNotificacao resultado, String motivoFalha) {
        this(UUID.randomUUID(), guiaId, canal, resultado, Instant.now(), motivoFalha);
    }

    public static HistoricoNotificacao restaurar(UUID id, UUID guiaId, CanalNotificacao canal,
                                                 ResultadoNotificacao resultado, Instant dataHora, String motivoFalha) {
        return new HistoricoNotificacao(id, guiaId, canal, resultado, dataHora, motivoFalha);
    }

    private void validar(UUID guiaId, CanalNotificacao canal, ResultadoNotificacao resultado,
                         String motivoFalha) {
        if (guiaId == null) {
            throw new IllegalArgumentException("guiaId é obrigatório");
        }
        if (canal == null) {
            throw new IllegalArgumentException("Canal é obrigatório");
        }
        if (resultado == null) {
            throw new IllegalArgumentException("Resultado é obrigatório");
        }
        if (resultado == ResultadoNotificacao.FALHA && (motivoFalha == null || motivoFalha.isBlank())) {
            throw new IllegalArgumentException("Motivo da falha é obrigatório quando resultado é FALHA");
        }
        if (resultado == ResultadoNotificacao.SUCESSO && motivoFalha != null && !motivoFalha.isBlank()) {
            throw new IllegalArgumentException("Resultado SUCESSO não deve ter motivo de falha");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getGuiaId() {
        return guiaId;
    }

    public CanalNotificacao getCanal() {
        return canal;
    }

    public ResultadoNotificacao getResultado() {
        return resultado;
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public String getMotivoFalha() {
        return motivoFalha;
    }
}