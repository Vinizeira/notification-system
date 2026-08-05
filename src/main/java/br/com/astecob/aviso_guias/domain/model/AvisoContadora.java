package br.com.astecob.aviso_guias.domain.model;

import java.time.Instant;
import java.util.UUID;

public class AvisoContadora {

    private final UUID id;
    private final String mensagem;
    private final Instant criadoEm;
    private final UUID guiaId;

    private AvisoContadora(UUID id, String mensagem, Instant criadoEm, UUID guiaId) {
        validar(mensagem);
        this.id = id;
        this.mensagem = mensagem;
        this.guiaId = guiaId;
        this.criadoEm = criadoEm;
    }

    public AvisoContadora(String mensagem, UUID guiaId) {
        this(UUID.randomUUID(), mensagem, Instant.now(), guiaId);
    }

    public static AvisoContadora restaurar(UUID id, String mensagem, Instant criadoEm, UUID guiaId) {
        return new AvisoContadora(id, mensagem, criadoEm, guiaId);
    }

    private void validar(String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            throw new IllegalArgumentException("Mensagem é obrigatória");
        }
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