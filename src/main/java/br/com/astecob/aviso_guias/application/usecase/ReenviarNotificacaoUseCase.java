package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.application.port.EmailSender;
import br.com.astecob.aviso_guias.domain.enums.CanalNotificacao;
import br.com.astecob.aviso_guias.domain.enums.ResultadoNotificacao;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.model.Guia;
import br.com.astecob.aviso_guias.domain.model.HistoricoNotificacao;
import br.com.astecob.aviso_guias.domain.repository.HistoricoNotificacaoRepository;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ReenviarNotificacaoUseCase {

    private final EmailSender emailSender;
    private final HistoricoNotificacaoRepository historicoRepository;

    public ReenviarNotificacaoUseCase(EmailSender emailSender,
                                      HistoricoNotificacaoRepository historicoRepository) {
        this.emailSender = emailSender;
        this.historicoRepository = historicoRepository;
    }

    public void executar(Guia guia, Cliente cliente, File arquivoPdf) {
        String assunto = "Sua guia de tributos - " + guia.getTipoGuia() + " (" + guia.getMes() + "/" + guia.getAno() + ")";
        String corpo = "Olá, " + cliente.getNomeEmpresa() + ".\n\nSegue em anexo a guia referente ao período.";

        boolean enviado = emailSender.enviar(cliente.getEmail(), assunto, corpo, arquivoPdf);

        ResultadoNotificacao resultado = enviado ? ResultadoNotificacao.SUCESSO : ResultadoNotificacao.FALHA;
        String motivoFalha = enviado ? null : "Falha ao reenviar e-mail via SMTP/Log";

        HistoricoNotificacao historico = new HistoricoNotificacao(
                guia.getId(),
                cliente.getNomeEmpresa(),
                cliente.getEmail(),
                cliente.getTelefoneWhatsapp(),
                CanalNotificacao.EMAIL,
                resultado,
                motivoFalha
        );

        historicoRepository.salvar(historico);
        if (!enviado) {
            throw new IllegalStateException("Não foi possível reenviar o e-mail. Verifique a conexão e tente novamente.");
        }
    }
}
