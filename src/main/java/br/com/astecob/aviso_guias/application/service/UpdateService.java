package br.com.astecob.aviso_guias.application.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarFile;
import java.util.jar.Attributes;
import java.util.logging.Logger;

@Service
public class UpdateService {

    private static final Logger LOGGER = Logger.getLogger(UpdateService.class.getName());

    @Value("${app.version}")
    private String versaoAtual;

    private static final String GIST_RAW_URL = "https://gist.githubusercontent.com/Vinizeira/b74197c59de238695eefa84f24a54808/raw/487bf5dd8051846cfbcce2ec08bf10e9348518cb/release.json";

    public void verificarAtualizacaoEDownload() {
        try {
            LOGGER.info("Verificando atualizações... Versão atual: " + versaoAtual);

            HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(GIST_RAW_URL)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                LOGGER.warning("Não foi possível verificar atualizações. Status: " + response.statusCode());
                return;
            }

            JsonMapper mapper = JsonMapper.builder().build();
            JsonNode jsonNode = mapper.readTree(response.body());

            JsonNode versionNode = jsonNode.get("versao");
            JsonNode urlNode = jsonNode.get("link");

            if (versionNode == null || urlNode == null) {
                LOGGER.warning("O JSON do Gist não contém as chaves 'versao' ou 'link'.");
                return;
            }

            String versaoRemota = versionNode.asText();
            String urlDownload = urlNode.asText();

            if (!versaoAtual.trim().equalsIgnoreCase(versaoRemota.trim())) {
                LOGGER.info("Nova versão encontrada: " + versaoRemota + ". Baixando atualização...");
                baixarEstrategiaAtualizacao(urlDownload);
            } else {
                LOGGER.info("O sistema já está na versão mais recente.");
            }

        } catch (Exception e) {
            LOGGER.severe("Erro ao verificar atualizações: " + e.getMessage());
        }
    }

    private void baixarEstrategiaAtualizacao(String fileUrl) {
        try {
            baixarEValidarAtualizacao(URI.create(fileUrl), Path.of("./update_novo.jar"));
            LOGGER.info("Download da nova versão validado e concluído em: ./update_novo.jar");
        } catch (Exception e) {
            LOGGER.severe("Erro ao baixar o arquivo de atualização: " + e.getMessage());
        }
    }

    void baixarEValidarAtualizacao(URI origem, Path destino) throws IOException {
        Path destinoAbsoluto = destino.toAbsolutePath();
        Path temporario = Files.createTempFile(destinoAbsoluto.getParent(), "update-", ".part");
        try {
            try (InputStream in = origem.toURL().openStream()) {
                Files.copy(in, temporario, StandardCopyOption.REPLACE_EXISTING);
            }
            try (InputStream in = Files.newInputStream(temporario)) {
                if (in.read() != 'P' || in.read() != 'K' || in.read() != 3 || in.read() != 4) {
                    throw new IOException("Atualização inválida: o arquivo não possui formato JAR/ZIP.");
                }
            }
            try (JarFile jar = new JarFile(temporario.toFile())) {
                var manifesto = jar.getManifest();
                String classePrincipal = manifesto == null ? null
                        : manifesto.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
                if (classePrincipal == null || classePrincipal.isBlank()
                        || jar.getJarEntry(classePrincipal.replace('.', '/') + ".class") == null) {
                    throw new IOException("Atualização inválida: o JAR não contém uma classe principal executável.");
                }
            }
            try {
                Files.move(temporario, destinoAbsoluto, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporario, destinoAbsoluto, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporario);
        }
    }
}
