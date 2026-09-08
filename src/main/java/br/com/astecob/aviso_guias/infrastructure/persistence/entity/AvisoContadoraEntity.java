package br.com.astecob.aviso_guias.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "avisos_contadora")
public class AvisoContadoraEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String mensagem;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    @Column(name = "guia_id")
    private UUID guiaId;

    protected AvisoContadoraEntity() {
    }

    public AvisoContadoraEntity(UUID id, String mensagem, Instant criadoEm, UUID guiaId) {
        this.id = id;
        this.mensagem = mensagem;
        this.criadoEm = criadoEm;
        this.guiaId = guiaId;
    }

    public UUID getId() {
        return id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public UUID getGuiaId() {
        return guiaId;
    }
}