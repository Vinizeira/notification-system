package br.com.astecob.aviso_guias;

import br.com.astecob.aviso_guias.application.port.EmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class AvisoGuiasApplicationTests {

	@MockitoBean
	private EmailSender emailSender;

	@Test
	void contextLoads() {
	}
}