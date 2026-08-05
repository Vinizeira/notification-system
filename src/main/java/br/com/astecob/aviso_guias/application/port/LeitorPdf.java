package br.com.astecob.aviso_guias.application.port;

import java.io.IOException;
import java.nio.file.Path;

public interface LeitorPdf {

    String lerTexto(Path caminhoArquivo) throws IOException;
}