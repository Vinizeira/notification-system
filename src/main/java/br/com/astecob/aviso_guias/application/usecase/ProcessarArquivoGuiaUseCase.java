package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.application.port.LeitorPdf;
import br.com.astecob.aviso_guias.domain.model.DadosNomeArquivoGuia;
import br.com.astecob.aviso_guias.domain.model.GuiaExtraida;
import br.com.astecob.aviso_guias.domain.service.ExtratorVencimentoGuia;
import br.com.astecob.aviso_guias.domain.service.NomeArquivoGuiaParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

@Component
public class ProcessarArquivoGuiaUseCase {

    private final NomeArquivoGuiaParser nomeArquivoGuiaParser;
    private final LeitorPdf leitorPdf;
    private final ExtratorVencimentoGuia extratorVencimentoGuia;

    public ProcessarArquivoGuiaUseCase(NomeArquivoGuiaParser nomeArquivoGuiaParser,
                                       LeitorPdf leitorPdf,
                                       ExtratorVencimentoGuia extratorVencimentoGuia) {
        this.nomeArquivoGuiaParser = nomeArquivoGuiaParser;
        this.leitorPdf = leitorPdf;
        this.extratorVencimentoGuia = extratorVencimentoGuia;
    }

    public GuiaExtraida processar(Path caminhoArquivo) throws IOException {
        String nomeArquivo = caminhoArquivo.getFileName().toString();

        DadosNomeArquivoGuia dadosNome = nomeArquivoGuiaParser.parse(nomeArquivo);

        String textoPdf = leitorPdf.lerTexto(caminhoArquivo);

        LocalDate vencimento = extratorVencimentoGuia.extrair(textoPdf);

        return new GuiaExtraida(
                dadosNome.getTipoGuia(),
                dadosNome.getNomeEmpresa(),
                dadosNome.getMes(),
                dadosNome.getAno(),
                caminhoArquivo,
                vencimento
        );
    }
}