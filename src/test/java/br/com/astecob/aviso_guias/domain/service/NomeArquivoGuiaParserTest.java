package br.com.astecob.aviso_guias.domain.service;

import br.com.astecob.aviso_guias.domain.model.DadosNomeArquivoGuia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NomeArquivoGuiaParserTest {

    private final NomeArquivoGuiaParser parser = new NomeArquivoGuiaParser();

    @Test
    void deveExtrairDadosDeNomeValido() {
        DadosNomeArquivoGuia dados = parser.parse("FGTS-Empresa Exemplo-06-2026.pdf");

        assertEquals("FGTS", dados.getTipoGuia());
        assertEquals("Empresa Exemplo", dados.getNomeEmpresa());
        assertEquals(6, dados.getMes());
        assertEquals(2026, dados.getAno());
    }

    @Test
    void deveExtrairEmpresaComHifen() {
        DadosNomeArquivoGuia dados = parser.parse("INSS-Grupo-Empresa Exemplo-Ltda-06-2026.pdf");

        assertEquals("INSS", dados.getTipoGuia());
        assertEquals("Grupo-Empresa Exemplo-Ltda", dados.getNomeEmpresa());
        assertEquals(6, dados.getMes());
        assertEquals(2026, dados.getAno());
    }

    @Test
    void deveRejeitarExtensaoInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("FGTS-Empresa Exemplo-06-2026.txt"));
    }

    @Test
    void deveRejeitarNomeIncompleto() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("FGTS-2026.pdf"));
    }

    @Test
    void deveRejeitarMesZero() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("FGTS-Empresa Exemplo-00-2026.pdf"));
    }

    @Test
    void deveRejeitarMesTreze() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("FGTS-Empresa Exemplo-13-2026.pdf"));
    }
}