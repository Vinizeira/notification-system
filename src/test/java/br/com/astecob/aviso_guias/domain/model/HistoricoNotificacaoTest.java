package br.com.astecob.aviso_guias.domain.model;

import br.com.astecob.aviso_guias.domain.enums.CanalNotificacao;
import br.com.astecob.aviso_guias.domain.enums.ResultadoNotificacao;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HistoricoNotificacaoTest {

    @Test
    void deveCriarHistoricoDeSucesso() {
        HistoricoNotificacao historico = new HistoricoNotificacao(
                UUID.randomUUID(), CanalNotificacao.EMAIL, ResultadoNotificacao.SUCESSO, null);

        assertNotNull(historico.getId());
        assertNotNull(historico.getDataHora());
        assertNull(historico.getMotivoFalha());
    }

    @Test
    void deveCriarHistoricoDeFalhaComMotivo() {
        HistoricoNotificacao historico = new HistoricoNotificacao(
                UUID.randomUUID(), CanalNotificacao.WHATSAPP, ResultadoNotificacao.FALHA, "API indisponível");

        assertEquals("API indisponível", historico.getMotivoFalha());
    }

    @Test
    void deveRejeitarFalhaSemMotivo() {
        assertThrows(IllegalArgumentException.class, () ->
                new HistoricoNotificacao(UUID.randomUUID(), CanalNotificacao.EMAIL,
                        ResultadoNotificacao.FALHA, null));
    }

    @Test
    void deveRejeitarSucessoComMotivo() {
        assertThrows(IllegalArgumentException.class, () ->
                new HistoricoNotificacao(UUID.randomUUID(), CanalNotificacao.EMAIL,
                        ResultadoNotificacao.SUCESSO, "não deveria ter motivo"));
    }

    @Test
    void deveRejeitarGuiaIdNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new HistoricoNotificacao(null, CanalNotificacao.EMAIL, ResultadoNotificacao.SUCESSO, null));
    }
}