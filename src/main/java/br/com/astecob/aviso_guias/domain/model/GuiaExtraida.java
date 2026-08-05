package br.com.astecob.aviso_guias.domain.model;

import java.nio.file.Path;
import java.time.LocalDate;

public class GuiaExtraida {

    private final String tipoGuia;
    private final String nomeEmpresa;
    private final int mes;
    private final int ano;
    private final Path caminhoArquivo;
    private final LocalDate dataVencimento;

    public GuiaExtraida(String tipoGuia, String nomeEmpresa, int mes, int ano,
                        Path caminhoArquivo, LocalDate dataVencimento) {
        this.tipoGuia = tipoGuia;
        this.nomeEmpresa = nomeEmpresa;
        this.mes = mes;
        this.ano = ano;
        this.caminhoArquivo = caminhoArquivo;
        this.dataVencimento = dataVencimento;
    }

    public String getTipoGuia() {
        return tipoGuia;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public Path getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }
}
