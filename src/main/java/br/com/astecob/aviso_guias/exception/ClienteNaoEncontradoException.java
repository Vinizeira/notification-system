package br.com.astecob.aviso_guias.exception;

import java.util.UUID;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(UUID id) {
        super("Cliente não encontrado: " + id);
    }
}
