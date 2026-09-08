package br.com.astecob.aviso_guias.domain.service;

import br.com.astecob.aviso_guias.domain.model.DadosNomeArquivoGuia;

import java.util.ArrayList;
import java.util.List;

public class NomeArquivoGuiaParser {

    public DadosNomeArquivoGuia parse(String nomeArquivo) {
        if (nomeArquivo == null || !nomeArquivo.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Arquivo deve ter extensão .pdf: " + nomeArquivo);
        }

        String semExtensao = nomeArquivo.substring(0, nomeArquivo.length() - 4);
        String[] partes = semExtensao.split("-");

        if (partes.length < 4) {
            throw new IllegalArgumentException("Nome de arquivo incompleto: " + nomeArquivo);
        }

        String tipoGuia = partes[0].trim();
        String mesStr = partes[partes.length - 2].trim();
        String anoStr = partes[partes.length - 1].trim();

        List<String> partesEmpresa = new ArrayList<>();
        for (int i = 1; i < partes.length - 2; i++) {
            partesEmpresa.add(partes[i].trim());
        }
        String nomeEmpresa = String.join("-", partesEmpresa);

        if (tipoGuia.isEmpty() || nomeEmpresa.isEmpty()) {
            throw new IllegalArgumentException("Nome de arquivo incompleto: " + nomeArquivo);
        }

        int mes;
        int ano;
        try {
            mes = Integer.parseInt(mesStr);
            ano = Integer.parseInt(anoStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Mês ou ano inválido: " + nomeArquivo);
        }

        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês inválido: " + mes);
        }

        return new DadosNomeArquivoGuia(tipoGuia, nomeEmpresa, mes, ano);
    }
}