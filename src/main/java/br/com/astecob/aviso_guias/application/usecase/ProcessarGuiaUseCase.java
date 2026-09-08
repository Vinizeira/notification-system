package br.com.astecob.aviso_guias.application.usecase;

import br.com.astecob.aviso_guias.application.service.NotificacaoOrchestrator;
import br.com.astecob.aviso_guias.domain.enums.StatusGuia;
import br.com.astecob.aviso_guias.domain.model.Cliente;
import br.com.astecob.aviso_guias.domain.model.Guia;
import br.com.astecob.aviso_guias.domain.model.GuiaExtraida;
import br.com.astecob.aviso_guias.domain.repository.ClienteRepository;
import br.com.astecob.aviso_guias.domain.repository.GuiaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProcessarGuiaUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ProcessarGuiaUseCase.class);

    private final GuiaRepository guiaRepository;
    private final ClienteRepository clienteRepository;
    private final NotificacaoOrchestrator notificacaoOrchestrator;
    private final AvisarContadoraUseCase avisarContadoraUseCase;

    public ProcessarGuiaUseCase(GuiaRepository guiaRepository, ClienteRepository clienteRepository,
                                NotificacaoOrchestrator notificacaoOrchestrator,
                                AvisarContadoraUseCase avisarContadoraUseCase) {
        this.guiaRepository = guiaRepository;
        this.clienteRepository = clienteRepository;
        this.notificacaoOrchestrator = notificacaoOrchestrator;
        this.avisarContadoraUseCase = avisarContadoraUseCase;
    }

    public void processar(GuiaExtraida guiaExtraida) {
        String nomeNormalizado = Cliente.normalizarNome(guiaExtraida.getNomeEmpresa());

        Optional<Guia> guiaExistenteOpt = guiaRepository.buscarPorChaveNatural(
                guiaExtraida.getTipoGuia(), nomeNormalizado, guiaExtraida.getMes(), guiaExtraida.getAno());

        // Se a guia já foi totalmente processada/enviada com sucesso no passado, ignora
        if (guiaExistenteOpt.isPresent() && guiaExistenteOpt.get().getStatus() != StatusGuia.PENDENTE_CLIENTE) {
            logger.info("Guia já processada e enviada anteriormente: {} - {} - {}/{}",
                    guiaExtraida.getTipoGuia(), nomeNormalizado, guiaExtraida.getMes(), guiaExtraida.getAno());
            return;
        }

        Optional<Cliente> clienteOpt = clienteRepository.buscarPorNomeEmpresaNormalizado(nomeNormalizado);

        // Se o cliente continua sem cadastro no banco
        if (clienteOpt.isEmpty()) {
            if (guiaExistenteOpt.isEmpty()) {
                Guia guiaPendente = new Guia(guiaExtraida.getTipoGuia(), nomeNormalizado,
                        guiaExtraida.getMes(), guiaExtraida.getAno(), guiaExtraida.getDataVencimento(),
                        null, StatusGuia.PENDENTE_CLIENTE);
                guiaRepository.salvar(guiaPendente);

                avisarContadoraUseCase.avisarClienteNaoEncontrado(
                        guiaExtraida.getTipoGuia(), guiaExtraida.getNomeEmpresa(),
                        guiaExtraida.getMes(), guiaExtraida.getAno());
            }
            return;
        }

        // Se o cliente agora existe, recupera a guia existente ou cria uma nova
        Cliente cliente = clienteOpt.get();
        Guia guia;

        if (guiaExistenteOpt.isPresent()) {
            guia = guiaExistenteOpt.get();
            guia.vincularCliente(cliente.getId());
        } else {
            guia = new Guia(guiaExtraida.getTipoGuia(), nomeNormalizado,
                    guiaExtraida.getMes(), guiaExtraida.getAno(), guiaExtraida.getDataVencimento(),
                    cliente.getId(), StatusGuia.PENDENTE_CLIENTE);
        }

        guiaRepository.salvar(guia);

        // Notifica o cliente por E-mail / WhatsApp
        notificacaoOrchestrator.processar(guia, cliente, guiaExtraida.getCaminhoArquivo().toFile());
        guiaRepository.salvar(guia);
    }
}