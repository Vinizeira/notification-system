package br.com.astecob.aviso_guias.application.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MensagemGuiaFactoryTest {

    private final MensagemGuiaFactory factory = new MensagemGuiaFactory();

    @Test
    void deveMontarMensagemComFormatoCorreto() {
        String mensagem = factory.mensagemWhatsApp("FGTS", LocalDate.of(2026, 6, 20));

        assertEquals("Enviei por e-mail a guia FGTS, com vencimento dia 20/06/2026.", mensagem);
    }
}