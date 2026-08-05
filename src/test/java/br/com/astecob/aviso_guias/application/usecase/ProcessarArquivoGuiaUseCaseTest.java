package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.application.port.LeitorPdf;
import br.com.astecob.aviso_guias.domain.model.GuiaExtraida;
import br.com.astecob.aviso_guias.domain.service.ExtratorVencimentoGuia;
import br.com.astecob.aviso_guias.domain.service.NomeArquivoGuiaParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessarArquivoGuiaUseCaseTest {

    @Spy
    private NomeArquivoGuiaParser nomeArquivoGuiaParser = new NomeArquivoGuiaParser();

    @Spy
    private ExtratorVencimentoGuia extratorVencimentoGuia = new ExtratorVencimentoGuia();

    @Mock
    private LeitorPdf leitorPdf;

    @InjectMocks
    private ProcessarArquivoGuiaUseCase useCase;

    @Test
    void deveProcessarArquivoValidoComSucesso() throws IOException {
        Path caminho = Path.of("FGTS-Astecob-06-2026.pdf");
        when(leitorPdf.lerTexto(caminho))
                .thenReturn("Pagar este documento até 20/07/2026");

        GuiaExtraida guia = useCase.processar(caminho);

        assertEquals("FGTS", guia.getTipoGuia());
        assertEquals("Astecob", guia.getNomeEmpresa());
        assertEquals(6, guia.getMes());
        assertEquals(2026, guia.getAno());
        assertEquals(LocalDate.of(2026, 7, 20), guia.getDataVencimento());
    }

    @Test
    void deveLancarExcecaoQuandoLeitorPdfFalha() throws IOException {
        Path caminho = Path.of("FGTS-Astecob-06-2026.pdf");
        when(leitorPdf.lerTexto(any())).thenThrow(new IOException("Falha ao ler PDF"));

        assertThrows(IOException.class, () -> useCase.processar(caminho));
    }

    @Test
    void deveLancarExcecaoQuandoNomeDoArquivoForInvalido() {
        Path caminho = Path.of("arquivo-invalido.txt");

        assertThrows(IllegalArgumentException.class, () -> useCase.processar(caminho));
    }
}