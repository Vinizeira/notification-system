package br.com.astecob.aviso_guias.infrastructure.email;

import br.com.astecob.aviso_guias.application.port.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Profile("prod")
@Component
public class GmailSmtpEmailSender implements EmailSender {

    private static final Logger log = LoggerFactory.getLogger(GmailSmtpEmailSender.class);
    private static final String REMETENTE = "astecob@gmail.com";

    private final JavaMailSender mailSender;

    public GmailSmtpEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean enviar(String destinatario, String assunto, String corpo, File anexoPdf) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(REMETENTE);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(corpo);
            helper.addAttachment(anexoPdf.getName(), new FileSystemResource(anexoPdf));

            mailSender.send(message);
            log.info("E-mail enviado com sucesso para {}", destinatario);
            return true;

        } catch (MailException | jakarta.mail.MessagingException e) {
            log.error("Falha ao enviar e-mail para {}: {}", destinatario, e.getMessage());
            return false;
        }
    }
}