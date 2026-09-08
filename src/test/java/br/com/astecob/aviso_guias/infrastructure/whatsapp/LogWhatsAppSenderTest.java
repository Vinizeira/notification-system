package br.com.astecob.aviso_guias.infrastructure.whatsapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogWhatsAppSenderTest {

    private final LogWhatsAppSender sender = new LogWhatsAppSender();

    @Test
    void deveRetornarSucessoAoEnviarMensagem() {
        boolean resultado = sender.enviar(
                "5521999999999", "Enviei por e-mail a guia FGTS, com vencimento dia 20/06/2026.");

        assertTrue(resultado);
    }
}