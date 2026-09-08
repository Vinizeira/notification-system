package br.com.astecob.aviso_guias.infrastructure.watcher;

import br.com.astecob.aviso_guias.application.usecase.ProcessarArquivoGuiaUseCase;
import br.com.astecob.aviso_guias.application.usecase.ProcessarGuiaUseCase;
import br.com.astecob.aviso_guias.domain.model.GuiaExtraida;
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
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuiaDirectoryWatcherTest {

    @Mock
    private ProcessarArquivoGuiaUseCase processarArquivoGuiaUseCase;

    @Mock
    private ProcessarGuiaUseCase processarGuiaUseCase;

    @Mock
    private GerenciadorArquivoGuia gerenciadorArquivoGuia;

    private Path pastaTemporaria;
    private GuiaDirectoryWatcher watcher;

    @BeforeEach
    void setUp() throws IOException {
        pastaTemporaria = Files.createTempDirectory("guias-teste");

        WatcherProperties properties = new WatcherProperties();
        properties.setDiretorio(pastaTemporaria.toString());

        watcher = new GuiaDirectoryWatcher(
                processarArquivoGuiaUseCase,
                processarGuiaUseCase,
                properties,
                gerenciadorArquivoGuia
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        try (var arquivos = Files.list(pastaTemporaria)) {
            arquivos.forEach(caminho -> caminho.toFile().delete());
        }
        Files.deleteIfExists(pastaTemporaria);
    }

    @Test
    void deveChamarUseCaseEGerenciadorQuandoPdfValidoForCriado() throws Exception {
        watcher.iniciar();

        Path arquivo = pastaTemporaria.resolve("FGTS-Empresa Exemplo-06-2026.pdf");
        Files.writeString(arquivo, "conteudo de teste");

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(processarArquivoGuiaUseCase, atLeastOnce()).processar(ArgumentMatchers.eq(arquivo));
            verify(gerenciadorArquivoGuia, atLeastOnce()).moverParaProcessados(ArgumentMatchers.eq(arquivo));
        });
    }

    @Test
    void deveIgnorarArquivoQueNaoTerminaEmPdf() throws Exception {
        watcher.iniciar();

        Path arquivo = pastaTemporaria.resolve("documento.txt");
        Files.writeString(arquivo, "conteudo de teste");

        TimeUnit.SECONDS.sleep(2);

        verify(processarArquivoGuiaUseCase, never()).processar(any());
        verify(gerenciadorArquivoGuia, never()).moverParaProcessados(any());
        verify(gerenciadorArquivoGuia, never()).moverParaErros(any());
    }

    @Test
    void naoDeveDerrubarWatcherEMoverParaErrosQuandoUseCaseLancaErro() throws Exception {
        when(processarArquivoGuiaUseCase.processar(any()))
                .thenThrow(new IllegalArgumentException("arquivo inválido"));

        watcher.iniciar();

        Path arquivo = pastaTemporaria.resolve("FGTS-Empresa Exemplo-06-2026.pdf");
        Files.writeString(arquivo, "conteudo de teste");

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(processarArquivoGuiaUseCase, atLeastOnce()).processar(any());
            verify(gerenciadorArquivoGuia, atLeastOnce()).moverParaErros(ArgumentMatchers.eq(arquivo));
        });

        reset(processarArquivoGuiaUseCase);

        Path segundoArquivo = pastaTemporaria.resolve("INSS-Empresa Exemplo-07-2026.pdf");
        Files.writeString(segundoArquivo, "conteudo de teste 2");

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(processarArquivoGuiaUseCase, atLeastOnce()).processar(ArgumentMatchers.eq(segundoArquivo))
        );
    }

    @Test
    void naoDeveProcessarMesmoArquivoDuasVezes() throws Exception {
        watcher.iniciar();

        Path arquivo = pastaTemporaria.resolve("FGTS-Empresa Exemplo-06-2026.pdf");
        Files.writeString(arquivo, "conteudo de teste");

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(processarArquivoGuiaUseCase, atLeastOnce()).processar(ArgumentMatchers.eq(arquivo))
        );

        watcher.iniciar();

        TimeUnit.SECONDS.sleep(2);

        verify(processarArquivoGuiaUseCase, times(1)).processar(ArgumentMatchers.eq(arquivo));
    }

    @Test
    void deveProcessarNovamenteArquivoRecolocadoAposConclusao() throws Exception {
        Path arquivo = pastaTemporaria.resolve("FGTS-Empresa Exemplo-06-2026.pdf");
        doAnswer(invocation -> {
            Files.delete(invocation.getArgument(0, Path.class));
            return null;
        }).when(gerenciadorArquivoGuia).moverParaProcessados(arquivo);

        Files.writeString(arquivo, "primeira guia de teste");
        watcher.iniciar();
        verify(processarArquivoGuiaUseCase, times(1)).processar(arquivo);

        Files.writeString(arquivo, "guia de teste recolocada");

        await().atMost(5, TimeUnit.SECONDS).until(() -> Files.notExists(arquivo));
        verify(processarArquivoGuiaUseCase, times(2)).processar(arquivo);
        verify(gerenciadorArquivoGuia, times(2)).moverParaProcessados(arquivo);
    }

    @Test
    void deveChamarProcessarGuiaUseCaseEMoverParaProcessadosAposExtracaoComSucesso() throws Exception {
        Path arquivo = pastaTemporaria.resolve("FGTS-Empresa Exemplo-06-2026.pdf");

        GuiaExtraida guiaExtraida = new GuiaExtraida(
                "FGTS", "Empresa Exemplo", 6, 2026, arquivo, LocalDate.of(2026, 6, 20));

        when(processarArquivoGuiaUseCase.processar(any())).thenReturn(guiaExtraida);

        watcher.iniciar();

        Files.writeString(arquivo, "conteudo de teste");

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(processarGuiaUseCase, atLeastOnce()).processar(guiaExtraida);
            verify(gerenciadorArquivoGuia, atLeastOnce()).moverParaProcessados(ArgumentMatchers.eq(arquivo));
        });
    }
}
