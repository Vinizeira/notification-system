package br.com.astecob.aviso_guias.domain.model;

import br.com.astecob.aviso_guias.domain.enums.CanalNotificacao;
import br.com.astecob.aviso_guias.domain.enums.ResultadoNotificacao;

import java.time.Instant;
import java.util.UUID;

public class HistoricoNotificacao {

    private final UUID id;
    private final UUID guiaId;
    private final String nomeEmpresa;
    private final String email;
    private final String telefoneWhatsapp;
    private final CanalNotificacao canal;
    private final ResultadoNotificacao resultado;
    private final Instant dataHora;
    private final String motivoFalha;

    public HistoricoNotificacao(UUID id, UUID guiaId, String nomeEmpresa, String email, String telefoneWhatsapp,
                                CanalNotificacao canal, ResultadoNotificacao resultado, Instant dataHora, String motivoFalha) {
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
            throw new IllegalArgumentException("Motivo da falha é obrigatório");
        }
        if (resultado == ResultadoNotificacao.SUCESSO && motivoFalha != null) {
            throw new IllegalArgumentException("Notificação de sucesso não deve ter motivo de falha");
        }

        this.id = id != null ? id : UUID.randomUUID();
        this.guiaId = guiaId;
        this.nomeEmpresa = nomeEmpresa;
        this.email = email;
        this.telefoneWhatsapp = telefoneWhatsapp;
        this.canal = canal;
        this.resultado = resultado;
        this.dataHora = dataHora != null ? dataHora : Instant.now();
        this.motivoFalha = motivoFalha;
    }

    public HistoricoNotificacao(UUID guiaId, String nomeEmpresa, String email, String telefoneWhatsapp,
                                CanalNotificacao canal, ResultadoNotificacao resultado, String motivoFalha) {
        this(UUID.randomUUID(), guiaId, nomeEmpresa, email, telefoneWhatsapp, canal, resultado, Instant.now(), motivoFalha);
    }

    public static HistoricoNotificacao restaurar(UUID id, UUID guiaId, String nomeEmpresa, String email, String telefoneWhatsapp,
                                                 CanalNotificacao canal, ResultadoNotificacao resultado, Instant dataHora, String motivoFalha) {
        return new HistoricoNotificacao(id, guiaId, nomeEmpresa, email, telefoneWhatsapp, canal, resultado, dataHora, motivoFalha);
    }

    public UUID getId() { return id; }
    public UUID getGuiaId() { return guiaId; }
    public String getNomeEmpresa() { return nomeEmpresa; }
    public String getEmail() { return email; }
    public String getTelefoneWhatsapp() { return telefoneWhatsapp; }
    public CanalNotificacao getCanal() { return canal; }
    public ResultadoNotificacao getResultado() { return resultado; }
    public Instant getDataHora() { return dataHora; }
    public String getMotivoFalha() { return motivoFalha; }
}
