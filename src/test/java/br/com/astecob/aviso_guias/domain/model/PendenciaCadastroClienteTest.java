package br.com.astecob.aviso_guias.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PendenciaCadastroClienteTest {

    @Test
    void deveCriarPendenciaValida() {
        PendenciaCadastroCliente pendencia = new PendenciaCadastroCliente("DAS", "Empresa Nova", 6, 2026);

        assertNotNull(pendencia.getId());
        assertNotNull(pendencia.getCriadoEm());
        assertEquals("Empresa Nova", pendencia.getNomeEmpresa());
    }

    @Test
    void deveRejeitarNomeEmpresaVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                new PendenciaCadastroCliente("DAS", "", 6, 2026));
    }

    @Test
    void deveRejeitarAnoInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                new PendenciaCadastroCliente("DAS", "Empresa Nova", 6, 1999));
    }
}