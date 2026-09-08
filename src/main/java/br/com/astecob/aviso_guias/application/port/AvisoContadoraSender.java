package br.com.astecob.aviso_guias.application.port;

import br.com.astecob.aviso_guias.domain.model.AvisoContadora;

public interface AvisoContadoraSender {

    /**
     * Avisa a contadora sobre falha de notificação ou pendência de cadastro.
     * Loga e persiste o aviso.
     */
    void avisar(AvisoContadora aviso);
}