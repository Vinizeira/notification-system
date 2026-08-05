package br.com.astecob.aviso_guias.infrastructure.watcher;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "astecob.guias")
public class WatcherProperties {

    private String diretorio = "./guias";

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }
}