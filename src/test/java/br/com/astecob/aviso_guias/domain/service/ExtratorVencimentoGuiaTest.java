package br.com.astecob.aviso_guias.domain.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExtratorVencimentoGuiaTest {

    private final ExtratorVencimentoGuia extrator = new ExtratorVencimentoGuia();

    @Test
    void deveExtrairVencimentoQuandoRotuloExiste() {
        String texto = "Guia de FGTS\nPagar este documento até 20/07/2026\nValor: R$ 500,00";

        LocalDate vencimento = extrator.extrair(texto);

        assertEquals(LocalDate.of(2026, 7, 20), vencimento);
    }

    @Test
    void deveLancarExcecaoQuandoRotuloNaoExiste() {
        String texto = "Guia sem rótulo de vencimento\nValor: R$ 500,00";

        assertThrows(IllegalArgumentException.class,
                () -> extrator.extrair(texto));
    }

    @Test
    void deveLancarExcecaoQuandoTextoForNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> extrator.extrair(null));
    }
}