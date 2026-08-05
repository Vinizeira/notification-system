package br.com.astecob.aviso_guias;

import br.com.astecob.aviso_guias.infrastructure.watcher.WatcherProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WatcherProperties.class)
public class AvisoGuiasApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvisoGuiasApplication.class, args);
	}

}
