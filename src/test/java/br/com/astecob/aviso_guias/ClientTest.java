package br.com.astecob.aviso_guias.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClienteTest {

    @Test
    void deveCriarClienteComNomeEmpresaNormalizado() {
        Cliente cliente = Cliente.novo(
                "  Açougue   São João  ",
                "contato@acougue.com",
                "11999999999"
        );

        assertNotNull(cliente.getId());
        assertNotNull(cliente.getCriadoEm());
        assertEquals("Açougue   São João", cliente.getNomeEmpresa());
        assertEquals(
                "ACOUGUE SAO JOAO",
                cliente.getNomeEmpresaNormalizado()
        );
    }

    @Test
    void deveRejeitarNomeEmpresaVazio() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Cliente.novo(
                        " ",
                        "contato@empresa.com",
                        "11999999999"
                )
        );
    }

    @Test
    void deveNormalizarNomeIgualAoClienteCriado() {
        Cliente cliente = Cliente.novo("Açaí & Cia - Ltda", "email@teste.com", "11999999999");

        String normalizadoDireto = Cliente.normalizarNome("Açaí & Cia - Ltda");

        assertEquals(cliente.getNomeEmpresaNormalizado(), normalizadoDireto);
    }

}