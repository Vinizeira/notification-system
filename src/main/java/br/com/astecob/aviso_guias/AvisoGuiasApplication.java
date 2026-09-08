package br.com.astecob.aviso_guias;

import br.com.astecob.aviso_guias.application.service.UpdateService;
import br.com.astecob.aviso_guias.infrastructure.watcher.WatcherProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(WatcherProperties.class)
public class AvisoGuiasApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvisoGuiasApplication.class, args);
	}

	@Bean
	public CommandLineRunner verificarAtualizacaoAoIniciar(UpdateService updateService) {
		return args -> {
			// Roda em segundo plano para não travar a inicialização do sistema
			new Thread(() -> updateService.verificarAtualizacaoEDownload()).start();
		};
	}
}
