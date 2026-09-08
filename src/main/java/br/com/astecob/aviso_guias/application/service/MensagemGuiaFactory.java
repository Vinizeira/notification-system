package br.com.astecob.aviso_guias.application.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class MensagemGuiaFactory {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String mensagemWhatsApp(String tipoGuia, LocalDate vencimento) {
        return "Enviei por e-mail a guia " + tipoGuia + ", com vencimento dia "
                + vencimento.format(FORMATO_DATA) + ".";
    }
}