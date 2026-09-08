package br.com.astecob.aviso_guias.application.port;

public interface WhatsAppSender {

    /**
     * Envia mensagem de WhatsApp ao cliente.
     * Retorna true em caso de sucesso, false em caso de falha.
     */
    boolean enviar(String numeroDestino, String mensagem);
}