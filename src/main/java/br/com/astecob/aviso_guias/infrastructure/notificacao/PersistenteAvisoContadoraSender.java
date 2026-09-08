package br.com.astecob.aviso_guias.infrastructure.notificacao;

import br.com.astecob.aviso_guias.application.port.AvisoContadoraSender;
import br.com.astecob.aviso_guias.domain.model.AvisoContadora;
import br.com.astecob.aviso_guias.domain.repository.AvisoContadoraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PersistenteAvisoContadoraSender implements AvisoContadoraSender {

    private static final Logger logger = LoggerFactory.getLogger(PersistenteAvisoContadoraSender.class);

    private final AvisoContadoraRepository avisoContadoraRepository;

    public PersistenteAvisoContadoraSender(AvisoContadoraRepository avisoContadoraRepository) {
        this.avisoContadoraRepository = avisoContadoraRepository;
    }

    @Override
    public void avisar(AvisoContadora aviso) {
        logger.warn("Aviso à contadora: {}", aviso.getMensagem());
        avisoContadoraRepository.salvar(aviso);
    }
}