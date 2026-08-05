package br.com.astecob.aviso_guias.domain.service;

import br.com.astecob.aviso_guias.domain.model.DadosNomeArquivoGuia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NomeArquivoGuiaParserTest {

    private final NomeArquivoGuiaParser parser = new NomeArquivoGuiaParser();

    @Test
    void deveExtrairDadosDeNomeValido() {
        DadosNomeArquivoGuia dados = parser.parse("FGTS-Astecob-06-2026.pdf");

        assertEquals("FGTS", dados.getTipoGuia());
        assertEquals("Astecob", dados.getNomeEmpresa());
        assertEquals(6, dados.getMes());
        assertEquals(2026, dados.getAno());
    }

    @Test
    void deveExtrairEmpresaComHifen() {
        DadosNomeArquivoGuia dados = parser.parse("INSS-Grupo-Astecob-Ltda-06-2026.pdf");

        assertEquals("INSS", dados.getTipoGuia());
        assertEquals("Grupo-Astecob-Ltda", dados.getNomeEmpresa());
        assertEquals(6, dados.getMes());
        assertEquals(2026, dados.getAno());
    }

    @Test
    void deveRejeitarExtensaoInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("FGTS-Astecob-06-2026.txt"));
    }

    @Test
    void deveRejeitarNomeIncompleto() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("FGTS-2026.pdf"));
    }

    @Test
    void deveRejeitarMesZero() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("FGTS-Astecob-00-2026.pdf"));
    }

    @Test
    void deveRejeitarMesTreze() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("FGTS-Astecob-13-2026.pdf"));
    }
}