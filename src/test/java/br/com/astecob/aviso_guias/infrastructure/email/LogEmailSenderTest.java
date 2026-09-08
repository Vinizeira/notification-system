package br.com.astecob.aviso_guias.infrastructure.email;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class LogEmailSenderTest {

    private final LogEmailSender sender = new LogEmailSender();

    @Test
    void deveRetornarSucessoAoEnviarComAnexo() {
        boolean resultado = sender.enviar(
                "cliente@example.com", "Guia FGTS", "Segue anexo", new File("FGTS-Empresa Exemplo-06-2026.pdf"));

        assertTrue(resultado);
    }

    @Test
    void deveRetornarSucessoMesmoSemAnexo() {
        boolean resultado = sender.enviar("cliente@example.com", "Guia FGTS", "Segue anexo", null);

        assertTrue(resultado);
    }
}