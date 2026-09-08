package br.com.astecob.aviso_guias.infrastructure.email;

import br.com.astecob.aviso_guias.application.port.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;

@Profile("dev")
@Component
public class LogEmailSender implements EmailSender {

    private static final Logger logger = LoggerFactory.getLogger(LogEmailSender.class);

    @Override
    public boolean enviar(String destinatario, String assunto, String corpo, File anexoPdf) {
        logger.info("Simulando envio de e-mail para {} | Assunto: {} | Anexo: {}",
                destinatario, assunto, anexoPdf != null ? anexoPdf.getName() : "nenhum");
        return true;
    }
}