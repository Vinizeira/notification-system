package br.com.astecob.aviso_guias.domain.model;

import br.com.astecob.aviso_guias.domain.enums.StatusGuia;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GuiaTest {

    @Test
    void deveCriarGuiaValida() {
        Guia guia = new Guia("FGTS", "ASTECOB", 6, 2026,
                LocalDate.of(2026, 6, 20), UUID.randomUUID(), StatusGuia.CONCLUIDA);

        assertNotNull(guia.getId());
        assertNotNull(guia.getCriadoEm());
        assertEquals("FGTS", guia.getTipoGuia());
        assertEquals(StatusGuia.CONCLUIDA, guia.getStatus());
    }

    @Test
    void devePermitirClienteIdNulo() {
        Guia guia = new Guia("FGTS", "ASTECOB", 6, 2026,
                LocalDate.of(2026, 6, 20), null, StatusGuia.PENDENTE_CLIENTE);

        assertNull(guia.getClienteId());
    }

    @Test
    void deveRejeitarTipoGuiaVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                new Guia("", "ASTECOB", 6, 2026, LocalDate.of(2026, 6, 20),
                        UUID.randomUUID(), StatusGuia.CONCLUIDA));
    }

    @Test
    void deveRejeitarMesInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                new Guia("FGTS", "ASTECOB", 13, 2026, LocalDate.of(2026, 6, 20),
                        UUID.randomUUID(), StatusGuia.CONCLUIDA));
    }

    @Test
    void deveRejeitarStatusNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Guia("FGTS", "ASTECOB", 6, 2026, LocalDate.of(2026, 6, 20),
                        UUID.randomUUID(), null));
    }

    @Test
    void deveAtualizarStatus() {
        Guia guia = new Guia("FGTS", "ASTECOB", 6, 2026,
                LocalDate.of(2026, 6, 20), UUID.randomUUID(), StatusGuia.CONCLUIDA);

        guia.atualizarStatus(StatusGuia.FALHA_EMAIL);

        assertEquals(StatusGuia.FALHA_EMAIL, guia.getStatus());
    }

    @Test
    void deveVincularCliente() {
        Guia guia = new Guia("FGTS", "ASTECOB", 6, 2026,
                LocalDate.of(2026, 6, 20), null, StatusGuia.PENDENTE_CLIENTE);
        UUID clienteId = UUID.randomUUID();

        guia.vincularCliente(clienteId);

        assertEquals(clienteId, guia.getClienteId());
    }
}