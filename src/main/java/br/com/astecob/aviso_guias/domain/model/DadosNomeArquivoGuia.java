package br.com.astecob.aviso_guias.domain.model;


public class DadosNomeArquivoGuia {

    private final String tipoGuia;
    private final String nomeEmpresa;
    private final int mes;
    private final int ano;

    public DadosNomeArquivoGuia(String tipoGuia, String nomeEmpresa, int mes, int ano) {
        this.tipoGuia = tipoGuia;
        this.nomeEmpresa = nomeEmpresa;
        this.mes = mes;
        this.ano = ano;
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
}
