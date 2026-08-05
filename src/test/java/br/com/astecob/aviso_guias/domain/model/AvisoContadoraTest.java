package br.com.astecob.aviso_guias.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AvisoContadoraTest {

    @Test
    void deveCriarAvisoComGuia() {
        UUID guiaId = UUID.randomUUID();
        AvisoContadora aviso = new AvisoContadora("E-mail falhou para a guia X", guiaId);

        assertNotNull(aviso.getId());
        assertEquals(guiaId, aviso.getGuiaId());
    }

    @Test
    void devePermitirGuiaIdNulo() {
        AvisoContadora aviso = new AvisoContadora("Aviso genérico", null);

        assertNull(aviso.getGuiaId());
    }

    @Test
    void deveRejeitarMensagemVazia() {
        assertThrows(IllegalArgumentException.class, () ->
                new AvisoContadora("", UUID.randomUUID()));
    }
}