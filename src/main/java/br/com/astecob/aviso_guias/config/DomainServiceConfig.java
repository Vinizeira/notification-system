package br.com.astecob.aviso_guias.config;

import br.com.astecob.aviso_guias.domain.service.ExtratorVencimentoGuia;
import br.com.astecob.aviso_guias.domain.service.NomeArquivoGuiaParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public NomeArquivoGuiaParser nomeArquivoGuiaParser() {
        return new NomeArquivoGuiaParser();
    }

    @Bean
    public ExtratorVencimentoGuia extratorVencimentoGuia() {
        return new ExtratorVencimentoGuia();
    }
}