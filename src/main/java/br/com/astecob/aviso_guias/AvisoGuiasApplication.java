package br.com.astecob.aviso_guias;

import br.com.astecob.aviso_guias.application.service.UpdateService;
import br.com.astecob.aviso_guias.infrastructure.watcher.WatcherProperties;
import org.springframework.beans.factory.ObjectProvider;
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
	public CommandLineRunner verificarAtualizacaoAoIniciar(
			ObjectProvider<UpdateService> updateServiceProvider) {

		return args -> updateServiceProvider.ifAvailable(updateService -> {
			Thread thread = new Thread(
					updateService::verificarAtualizacaoEDownload,
					"update-checker"
			);
			thread.setDaemon(true);
			thread.start();
		});
	}
}