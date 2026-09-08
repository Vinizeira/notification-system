package br.com.astecob.aviso_guias.exception;

public class ClienteDuplicadoException extends RuntimeException {

    public ClienteDuplicadoException(String nomeEmpresa) {
        super("Já existe um cliente cadastrado para a empresa: " + nomeEmpresa);
    }
}