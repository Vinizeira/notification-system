package br.com.astecob.aviso_guias.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "historico_notificacao")
public class HistoricoNotificacaoEntity {

    @Id
    private UUID id;

    @Column(name = "guia_id", nullable = false)
    private UUID guiaId;

    @Column(name = "nome_empresa")
    private String nomeEmpresa;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone_whatsapp")
    private String telefoneWhatsapp;

    @Column(nullable = false)
    private String canal;

    @Column(nullable = false)
    private String resultado;

    @Column(name = "data_hora", nullable = false)
    private Instant dataHora;

    @Column(name = "motivo_falha")
    private String motivoFalha;

    protected HistoricoNotificacaoEntity() {
    }

    public HistoricoNotificacaoEntity(UUID id, UUID guiaId, String nomeEmpresa, String email, String telefoneWhatsapp,
                                      String canal, String resultado, Instant dataHora, String motivoFalha) {
        this.id = id;
        this.guiaId = guiaId;
        this.nomeEmpresa = nomeEmpresa;
        this.email = email;
        this.telefoneWhatsapp = telefoneWhatsapp;
        this.canal = canal;
        this.resultado = resultado;
        this.dataHora = dataHora;
        this.motivoFalha = motivoFalha;
    }

    public UUID getId() { return id; }
    public UUID getGuiaId() { return guiaId; }
    public String getNomeEmpresa() { return nomeEmpresa; }
    public String getEmail() { return email; }
    public String getTelefoneWhatsapp() { return telefoneWhatsapp; }
    public String getCanal() { return canal; }
    public String getResultado() { return resultado; }
    public Instant getDataHora() { return dataHora; }
    public String getMotivoFalha() { return motivoFalha; }
}