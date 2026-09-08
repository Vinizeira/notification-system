package br.com.astecob.aviso_guias.application.port;

import java.io.File;

public interface EmailSender {

    /**
     * Envia o PDF da guia por e-mail ao cliente.
     * Retorna true em caso de sucesso, false em caso de falha.
     */
    boolean enviar(String destinatario, String assunto, String corpo, File anexoPdf);
}