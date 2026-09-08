package br.com.astecob.aviso_guias.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import static org.junit.jupiter.api.Assertions.*;

class UpdateServiceTest {

    @TempDir
    Path pasta;

    private final UpdateService service = new UpdateService();

    @Test
    void deveRejeitarHtmlGrandeEPreservarAtualizacaoAnterior() throws IOException {
        Path origem = pasta.resolve("download.html");
        Files.writeString(origem, "<html>" + "x".repeat(1_000_000) + "</html>");
        verificarDownloadInvalido(origem);
    }

    @Test
    void deveRejeitarArquivoTruncadoMesmoComAssinaturaZip() throws IOException {
        Path origem = pasta.resolve("truncado.jar");
        Files.write(origem, new byte[] {'P', 'K', 3, 4, 0, 0});
        verificarDownloadInvalido(origem);
    }

    @Test
    void deveRejeitarZipSemManifestoExecutavel() throws IOException {
        Path origem = pasta.resolve("arquivo.zip");
        try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(origem))) {
            out.putNextEntry(new JarEntry("exemplo.txt"));
            out.write(new byte[] {1, 2, 3});
            out.closeEntry();
        }
        verificarDownloadInvalido(origem);
    }

    @Test
    void deveSubstituirDestinoApenasAposValidarJarExecutavel() throws IOException {
        Path origem = pasta.resolve("valido.jar");
        Manifest manifesto = new Manifest();
        manifesto.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifesto.getMainAttributes().put(Attributes.Name.MAIN_CLASS, AplicacaoExemplo.class.getName());
        String classe = AplicacaoExemplo.class.getName().replace('.', '/') + ".class";
        try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(origem), manifesto);
             var in = AplicacaoExemplo.class.getResourceAsStream("/" + classe)) {
            assertNotNull(in);
            out.putNextEntry(new JarEntry(classe));
            in.transferTo(out);
            out.closeEntry();
        }
        Path destino = pasta.resolve("update_novo.jar");
        Files.writeString(destino, "download anterior");

        service.baixarEValidarAtualizacao(origem.toUri(), destino);

        assertArrayEquals(Files.readAllBytes(origem), Files.readAllBytes(destino));
        verificarTemporariosRemovidos();
    }

    private void verificarDownloadInvalido(Path origem) throws IOException {
        Path destino = pasta.resolve("update_novo.jar");
        Files.writeString(destino, "download anterior");

        assertThrows(IOException.class, () -> service.baixarEValidarAtualizacao(origem.toUri(), destino));

        assertEquals("download anterior", Files.readString(destino));
        verificarTemporariosRemovidos();
    }

    private void verificarTemporariosRemovidos() throws IOException {
        try (var arquivos = Files.list(pasta)) {
            assertTrue(arquivos.noneMatch(arquivo -> arquivo.toString().endsWith(".part")));
        }
    }

    public static class AplicacaoExemplo {
        public static void main(String[] args) {
        }
    }
}
