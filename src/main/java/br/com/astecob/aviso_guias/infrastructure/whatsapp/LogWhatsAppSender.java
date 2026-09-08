package br.com.astecob.aviso_guias.infrastructure.whatsapp;

import br.com.astecob.aviso_guias.application.port.WhatsAppSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogWhatsAppSender implements WhatsAppSender {

    private static final Logger logger = LoggerFactory.getLogger(LogWhatsAppSender.class);

    @Override
    public boolean enviar(String numeroDestino, String mensagem) {
        logger.info("Simulando envio de WhatsApp para {} | Mensagem: {}", numeroDestino, mensagem);
        return true;
    }
}