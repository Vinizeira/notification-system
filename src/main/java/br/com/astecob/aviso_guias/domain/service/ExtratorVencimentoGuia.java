package br.com.astecob.aviso_guias.domain.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtratorVencimentoGuia {

    private static final String ROTULO = "Pagar este documento até";
    private static final Pattern PADRAO_DATA = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public LocalDate extrair(String textoPdf) {
        if (textoPdf == null || !textoPdf.contains(ROTULO)) {
            throw new IllegalArgumentException("Arquivo não reconhecido como guia: rótulo de vencimento não encontrado");
        }

        int indiceRotulo = textoPdf.indexOf(ROTULO);
        String textoAposRotulo = textoPdf.substring(indiceRotulo + ROTULO.length());

        Matcher matcher = PADRAO_DATA.matcher(textoAposRotulo);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Arquivo não reconhecido como guia: data de vencimento não encontrada");
        }

        String dataEncontrada = matcher.group(1);
        return LocalDate.parse(dataEncontrada, FORMATO_DATA);
    }
}