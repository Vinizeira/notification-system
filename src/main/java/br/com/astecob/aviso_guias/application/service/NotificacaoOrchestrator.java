package br.com.astecob.aviso_guias.application.service;

import br.com.astecob.aviso_guias.application.port.AvisoContadoraSender;
import br.com.astecob.aviso_guias.application.port.EmailSender;
import br.com.astecob.aviso_guias.application.port.WhatsAppSender;
import br.com.astecob.aviso_guias.domain.enums.CanalNotificacao;
import br.com.astecob.aviso_guias.domain.enums.StatusGuia;
import br.com.astecob.aviso_guias.domain.model.AvisoContadora;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.model.Guia;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class NotificacaoOrchestrator {

    private final EmailSender emailSender;
    private final WhatsAppSender whatsAppSender;
    private final AvisoContadoraSender avisoContadoraSender;
    private final HistoricoNotificacaoService historicoNotificacaoService;
    private final MensagemGuiaFactory mensagemGuiaFactory;

    public NotificacaoOrchestrator(EmailSender emailSender, WhatsAppSender whatsAppSender,
                                   AvisoContadoraSender avisoContadoraSender,
                                   HistoricoNotificacaoService historicoNotificacaoService,
                                   MensagemGuiaFactory mensagemGuiaFactory) {
        this.emailSender = emailSender;
        this.whatsAppSender = whatsAppSender;
        this.avisoContadoraSender = avisoContadoraSender;
        this.historicoNotificacaoService = historicoNotificacaoService;
        this.mensagemGuiaFactory = mensagemGuiaFactory;
    }

    public void processar(Guia guia, Cliente cliente, File anexoPdf) {
        String assunto = "Guia " + guia.getTipoGuia();
        String corpo = "Segue em anexo a guia " + guia.getTipoGuia() + " com vencimento em "
                + guia.getVencimento() + ".";

        boolean emailEnviado = emailSender.enviar(cliente.getEmail(), assunto, corpo, anexoPdf);

        if (!emailEnviado) {
            historicoNotificacaoService.registrarFalha(guia.getId(), cliente, CanalNotificacao.EMAIL, "Falha no envio de e-mail");
            guia.atualizarStatus(StatusGuia.FALHA_EMAIL);
            avisoContadoraSender.avisar(new AvisoContadora(
                    "Falha ao enviar e-mail da guia " + guia.getTipoGuia() + " para " + cliente.getNomeEmpresa(),
                    guia.getId()));
            return;
        }

        historicoNotificacaoService.registrarSucesso(guia.getId(), cliente, CanalNotificacao.EMAIL);

        String mensagemWhatsApp = mensagemGuiaFactory.mensagemWhatsApp(guia.getTipoGuia(), guia.getVencimento());
        boolean whatsAppEnviado = whatsAppSender.enviar(cliente.getTelefoneWhatsapp(), mensagemWhatsApp);

        if (!whatsAppEnviado) {
            historicoNotificacaoService.registrarFalha(guia.getId(), cliente, CanalNotificacao.WHATSAPP, "Falha no envio de WhatsApp");
            guia.atualizarStatus(StatusGuia.PARCIAL_WHATSAPP_FALHOU);
            avisoContadoraSender.avisar(new AvisoContadora(
                    "E-mail enviado, mas WhatsApp falhou para a guia " + guia.getTipoGuia()
                            + " de " + cliente.getNomeEmpresa() + ". Envio manual necessário.",
                    guia.getId()));
            return;
        }

        historicoNotificacaoService.registrarSucesso(guia.getId(), cliente, CanalNotificacao.WHATSAPP);
        guia.atualizarStatus(StatusGuia.CONCLUIDA);
    }

    public void reenviar(Guia guia, Cliente cliente, File anexoPdf) {
        processar(guia, cliente, anexoPdf);
    }
}