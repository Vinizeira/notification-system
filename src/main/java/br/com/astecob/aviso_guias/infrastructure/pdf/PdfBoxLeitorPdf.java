package br.com.astecob.aviso_guias.infrastructure.pdf;

import br.com.astecob.aviso_guias.application.port.LeitorPdf;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class PdfBoxLeitorPdf implements LeitorPdf {

    @Override
    public String lerTexto(Path caminhoArquivo) throws IOException {
        try (PDDocument documento = Loader.loadPDF(caminhoArquivo.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(documento);
        }
    }
}