package br.com.astecob.aviso_guias.infrastructure.watcher;

import br.com.astecob.aviso_guias.application.usecase.ProcessarArquivoGuiaUseCase;
import br.com.astecob.aviso_guias.application.usecase.ProcessarGuiaUseCase;
import br.com.astecob.aviso_guias.domain.model.GuiaExtraida;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GuiaDirectoryWatcher {

    private static final Logger log = LoggerFactory.getLogger(GuiaDirectoryWatcher.class);

    private final ProcessarArquivoGuiaUseCase processarArquivoGuiaUseCase;
    private final ProcessarGuiaUseCase processarGuiaUseCase;
    private final WatcherProperties watcherProperties;
    private final GerenciadorArquivoGuia gerenciadorArquivoGuia;
    private final Set<Path> arquivosProcessados = ConcurrentHashMap.newKeySet();
    private Thread threadWatcher;

    public GuiaDirectoryWatcher(ProcessarArquivoGuiaUseCase processarArquivoGuiaUseCase,
                                ProcessarGuiaUseCase processarGuiaUseCase,
                                WatcherProperties watcherProperties,
                                GerenciadorArquivoGuia gerenciadorArquivoGuia) {
        this.processarArquivoGuiaUseCase = processarArquivoGuiaUseCase;
        this.processarGuiaUseCase = processarGuiaUseCase;
        this.watcherProperties = watcherProperties;
        this.gerenciadorArquivoGuia = gerenciadorArquivoGuia;
    }

    @PostConstruct
    public synchronized void iniciar() {
        if (threadWatcher != null && threadWatcher.isAlive()) {
            return;
        }

        Path diretorio = Path.of(watcherProperties.getDiretorio());

        try {
            if (Files.notExists(diretorio)) {
                Files.createDirectories(diretorio);
                log.info("Pasta de guias criada: {}", diretorio.toAbsolutePath());
            }

            processarArquivosExistentes(diretorio);

            WatchService watchService = FileSystems.getDefault().newWatchService();
            diretorio.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            threadWatcher = new Thread(() -> monitorar(diretorio, watchService));
            threadWatcher.setDaemon(true);
            threadWatcher.setName("guia-directory-watcher");
            threadWatcher.start();

            log.info("Watcher iniciado, monitorando: {}", diretorio.toAbsolutePath());
        } catch (IOException e) {
            log.error("Falha ao iniciar o watcher da pasta de guias", e);
        }
    }

    private void processarArquivosExistentes(Path diretorio) throws IOException {
        try (var arquivos = Files.list(diretorio)) {
            arquivos
                    .filter(caminho -> caminho.toString().toLowerCase().endsWith(".pdf"))
                    .forEach(this::processarSeNovo);
        }
    }

    private void monitorar(Path diretorio, WatchService watchService) {
        try (watchService) {
            while (true) {
                WatchKey chave = watchService.take();

                for (WatchEvent<?> evento : chave.pollEvents()) {
                    if (evento.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path nomeArquivo = (Path) evento.context();
                        Path caminhoCompleto = diretorio.resolve(nomeArquivo);

                        if (caminhoCompleto.toString().toLowerCase().endsWith(".pdf")) {
                            processarSeNovo(caminhoCompleto);
                        }
                    }
                }

                boolean chaveValida = chave.reset();
                if (!chaveValida) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("Watcher encerrado por erro inesperado", e);
            Thread.currentThread().interrupt();
        }
    }

    private void processarSeNovo(Path caminhoArquivo) {
        Path caminhoAbsoluto = caminhoArquivo.toAbsolutePath().normalize();

        if (!arquivosProcessados.add(caminhoAbsoluto)) {
            return;
        }

        try {
            if (!aguardarLiberacaoArquivo(caminhoArquivo)) {
                log.error("O arquivo continuou bloqueado pelo sistema operacional: {}", caminhoArquivo);
                return;
            }

            GuiaExtraida guiaExtraida = processarArquivoGuiaUseCase.processar(caminhoArquivo);

            if (guiaExtraida != null) {
                processarGuiaUseCase.processar(guiaExtraida);
            }

            log.info("Guia processada com sucesso: {}", caminhoArquivo);
            gerenciadorArquivoGuia.moverParaProcessados(caminhoArquivo);

        } catch (IllegalArgumentException e) {
            log.warn("Arquivo não reconhecido como guia: {} - motivo: {}", caminhoArquivo, e.getMessage());
            gerenciadorArquivoGuia.moverParaErros(caminhoArquivo);
        } catch (IOException e) {
            log.error("Erro ao ler PDF: {}", caminhoArquivo, e);
            gerenciadorArquivoGuia.moverParaErros(caminhoArquivo);
        } catch (Exception e) {
            log.error("Erro inesperado ao processar arquivo: {}", caminhoArquivo, e);
            gerenciadorArquivoGuia.moverParaErros(caminhoArquivo);
        } finally {
            // Remove do conjunto para permitir reprocessamentos futuros caso o arquivo seja recolocado
            arquivosProcessados.remove(caminhoAbsoluto);
        }
    }

    private boolean aguardarLiberacaoArquivo(Path path) {
        int tentativas = 0;
        int maxTentativas = 10;
        long tempoEsperaMs = 500;

        while (tentativas < maxTentativas) {
            try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
                return true;
            } catch (IOException e) {
                tentativas++;
                try {
                    Thread.sleep(tempoEsperaMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }
}
