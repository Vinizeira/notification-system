package br.com.astecob.aviso_guias;

import br.com.astecob.aviso_guias.application.port.EmailSender;
import br.com.astecob.aviso_guias.infrastructure.notificacao.WhatsAppService;
import br.com.astecob.aviso_guias.infrastructure.watcher.GuiaDirectoryWatcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class AvisoGuiasApplicationTests {

	@MockitoBean
	private EmailSender emailSender;

	@MockitoBean
	private WhatsAppService whatsAppService;

	@MockitoBean
	private GuiaDirectoryWatcher guiaDirectoryWatcher;

	@Test
	void contextLoads() {
	}
}