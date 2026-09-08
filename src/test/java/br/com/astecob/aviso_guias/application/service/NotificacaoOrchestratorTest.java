package br.com.astecob.aviso_guias.application.service;

import br.com.astecob.aviso_guias.application.port.AvisoContadoraSender;
import br.com.astecob.aviso_guias.application.port.EmailSender;
import br.com.astecob.aviso_guias.application.port.WhatsAppSender;
import br.com.astecob.aviso_guias.domain.enums.StatusGuia;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.model.Guia;
import br.com.astecob.aviso_guias.domain.repository.HistoricoNotificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoOrchestratorTest {

    @Mock
    private EmailSender emailSender;
    @Mock
    private WhatsAppSender whatsAppSender;
    @Mock
    private AvisoContadoraSender avisoContadoraSender;
    @Mock
    private HistoricoNotificacaoRepository historicoNotificacaoRepository;

    private NotificacaoOrchestrator orchestrator;
    private Guia guia;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        HistoricoNotificacaoService historicoService = new HistoricoNotificacaoService(historicoNotificacaoRepository);
        MensagemGuiaFactory factory = new MensagemGuiaFactory();
        orchestrator = new NotificacaoOrchestrator(emailSender, whatsAppSender, avisoContadoraSender, historicoService, factory);

            guia = new Guia("FGTS", "EMPRESAEX", 6, 2026, LocalDate.of(2026, 6, 20),
                UUID.randomUUID(), StatusGuia.PENDENTE_CLIENTE);
        cliente = Cliente.novo("Empresa", "contato@example.com", "5521999999999");
    }

    @Test
    void deveConcluirComSucessoQuandoEmailEWhatsAppFuncionam() {
        when(emailSender.enviar(any(), any(), any(), any())).thenReturn(true);
        when(whatsAppSender.enviar(any(), any())).thenReturn(true);

        orchestrator.processar(guia, cliente, new File("guia.pdf"));

        assertEquals(StatusGuia.CONCLUIDA, guia.getStatus());
        verify(historicoNotificacaoRepository, times(2)).salvar(any());
        verify(avisoContadoraSender, never()).avisar(any());
    }

    @Test
    void deveMarcarFalhaEmailENaoTentarWhatsApp() {
        when(emailSender.enviar(any(), any(), any(), any())).thenReturn(false);

        orchestrator.processar(guia, cliente, new File("guia.pdf"));

        assertEquals(StatusGuia.FALHA_EMAIL, guia.getStatus());
        verify(whatsAppSender, never()).enviar(any(), any());
        verify(avisoContadoraSender, times(1)).avisar(any());
    }

    @Test
    void deveMarcarParcialQuandoEmailOkEWhatsAppFalha() {
        when(emailSender.enviar(any(), any(), any(), any())).thenReturn(true);
        when(whatsAppSender.enviar(any(), any())).thenReturn(false);

        orchestrator.processar(guia, cliente, new File("guia.pdf"));

        assertEquals(StatusGuia.PARCIAL_WHATSAPP_FALHOU, guia.getStatus());
        verify(avisoContadoraSender, times(1)).avisar(any());
    }


}