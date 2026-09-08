package br.com.astecob.aviso_guias.infrastructure.notificacao;

import br.com.astecob.aviso_guias.domain.model.AvisoContadora;
import br.com.astecob.aviso_guias.domain.repository.AvisoContadoraRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersistenteAvisoContadoraSenderTest {

    @Mock
    private AvisoContadoraRepository avisoContadoraRepository;

    @Test
    void deveLogarEPersistirAviso() {
        PersistenteAvisoContadoraSender sender = new PersistenteAvisoContadoraSender(avisoContadoraRepository);
        AvisoContadora aviso = new AvisoContadora("E-mail falhou para a guia FGTS", UUID.randomUUID());

        sender.avisar(aviso);

        ArgumentCaptor<AvisoContadora> captor = ArgumentCaptor.forClass(AvisoContadora.class);
        verify(avisoContadoraRepository, times(1)).salvar(captor.capture());
        assertEquals(aviso.getMensagem(), captor.getValue().getMensagem());
    }
}