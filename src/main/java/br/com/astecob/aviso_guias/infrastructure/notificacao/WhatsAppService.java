package br.com.astecob.aviso_guias.infrastructure.notificacao;

import br.com.astecob.aviso_guias.application.port.WhatsAppSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Primary
@Service
public class WhatsAppService implements WhatsAppSender {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppService.class);

    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.api.token}")
    private String apiToken;

    @Value("${whatsapp.api.phone-number-id}")
    private String phoneNumberId;

    private final RestClient restClient = RestClient.create();

    @Override
    public boolean enviar(String numeroDestino, String mensagem) {
        String endpoint = String.format("%s/%s/messages", apiUrl, phoneNumberId);

        // Tratamento simples para garantir formato do numero (somente digitos)
        String numeroFormatado = numeroDestino.replaceAll("\\D", "");

        Map<String, Object> body = Map.of(
                "messaging_product", "whatsapp",
                "recipient_type", "individual",
                "to", numeroFormatado,
                "type", "text",
                "text", Map.of(
                        "preview_url", false,
                        "body", mensagem
                )
        );

        try {
            restClient.post()
                    .uri(endpoint)
                    .header("Authorization", "Bearer " + apiToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Mensagem WhatsApp enviada com sucesso para: {}", numeroFormatado);
            return true;
        } catch (Exception e) {
            log.error("Falha ao enviar mensagem de WhatsApp para {}: {}", numeroFormatado, e.getMessage());
            return false;
        }
    }
}