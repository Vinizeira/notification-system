package br.com.astecob.aviso_guias.infrastructure.watcher;

import br.com.astecob.aviso_guias.application.usecase.ProcessarArquivoGuiaUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuiaDirectoryWatcherTest {

    @Mock
    private ProcessarArquivoGuiaUseCase processarArquivoGuiaUseCase;

    private Path pastaTemporaria;
    private GuiaDirectoryWatcher watcher;

    @BeforeEach
    void setUp() throws IOException {
        pastaTemporaria = Files.createTempDirectory("guias-teste");

        WatcherProperties properties = new WatcherProperties();
        properties.setDiretorio(pastaTemporaria.toString());

        watcher = new GuiaDirectoryWatcher(processarArquivoGuiaUseCase, properties);
    }

    @AfterEach
    void tearDown() throws IOException {
        try (var arquivos = Files.list(pastaTemporaria)) {
            arquivos.forEach(caminho -> caminho.toFile().delete());
        }
        Files.deleteIfExists(pastaTemporaria);
    }

    @Test
    void deveChamarUseCaseQuandoPdfValidoForCriado() throws Exception {
        watcher.iniciar();

        Path arquivo = pastaTemporaria.resolve("FGTS-Astecob-06-2026.pdf");
        Files.writeString(arquivo, "conteudo de teste");

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(processarArquivoGuiaUseCase, atLeastOnce()).processar(ArgumentMatchers.eq(arquivo))
        );
    }

    @Test
    void deveIgnorarArquivoQueNaoTerminaEmPdf() throws Exception {
        watcher.iniciar();

        Path arquivo = pastaTemporaria.resolve("documento.txt");
        Files.writeString(arquivo, "conteudo de teste");

        TimeUnit.SECONDS.sleep(2);

        verify(processarArquivoGuiaUseCase, never()).processar(any());
    }

    @Test
    void naoDeveDerrubarWatcherQuandoUseCaseLancaErro() throws Exception {
        when(processarArquivoGuiaUseCase.processar(any()))
                .thenThrow(new IllegalArgumentException("arquivo inválido"));

        watcher.iniciar();

        Path arquivo = pastaTemporaria.resolve("FGTS-Astecob-06-2026.pdf");
        Files.writeString(arquivo, "conteudo de teste");

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(processarArquivoGuiaUseCase, atLeastOnce()).processar(any())
        );

        reset(processarArquivoGuiaUseCase);

        Path segundoArquivo = pastaTemporaria.resolve("INSS-Astecob-07-2026.pdf");
        Files.writeString(segundoArquivo, "conteudo de teste 2");

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(processarArquivoGuiaUseCase, atLeastOnce()).processar(ArgumentMatchers.eq(segundoArquivo))
        );
    }

    @Test
    void naoDeveProcessarMesmoArquivoDuasVezes() throws Exception {
        watcher.iniciar();

        Path arquivo = pastaTemporaria.resolve("FGTS-Astecob-06-2026.pdf");
        Files.writeString(arquivo, "conteudo de teste");

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(processarArquivoGuiaUseCase, atLeastOnce()).processar(ArgumentMatchers.eq(arquivo))
        );

        watcher.iniciar();

        TimeUnit.SECONDS.sleep(2);

        verify(processarArquivoGuiaUseCase, times(1)).processar(ArgumentMatchers.eq(arquivo));
    }
}