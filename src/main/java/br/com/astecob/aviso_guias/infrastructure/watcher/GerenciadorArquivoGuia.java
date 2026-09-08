package br.com.astecob.aviso_guias.infrastructure.watcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class GerenciadorArquivoGuia {

    private static final Logger log = LoggerFactory.getLogger(GerenciadorArquivoGuia.class);

    private final Path pastaProcessados;
    private final Path pastaErros;

    public GerenciadorArquivoGuia(
            @Value("${app.diretorio.processados:./processados}") String caminhoProcessados,
            @Value("${app.diretorio.erros:./erros}") String caminhoErros) {
        this.pastaProcessados = Path.of(caminhoProcessados).toAbsolutePath().normalize();
        this.pastaErros = Path.of(caminhoErros).toAbsolutePath().normalize();
    }

    public void moverParaProcessados(Path caminhoArquivo) {
        moverArquivo(caminhoArquivo, pastaProcessados);
    }

    public void moverParaErros(Path caminhoArquivo) {
        moverArquivo(caminhoArquivo, pastaErros);
    }

    private void moverArquivo(Path origem, Path pastaDestino) {
        try {
            Path origemAbsoluta = origem.toAbsolutePath().normalize();

            if (!Files.exists(origemAbsoluta)) {
                log.warn("Arquivo não encontrado para mover: {}", origemAbsoluta);
                return;
            }

            Files.createDirectories(pastaDestino);
            Path destino = pastaDestino.resolve(origemAbsoluta.getFileName());

            Files.move(origemAbsoluta, destino, StandardCopyOption.REPLACE_EXISTING);
            log.info("Arquivo movido com sucesso de [{}] para [{}]", origemAbsoluta, destino);

        } catch (IOException e) {
            log.error("Erro ao mover arquivo [{}] para [{}]: {}", origem, pastaDestino, e.getMessage(), e);
        }
    }
}