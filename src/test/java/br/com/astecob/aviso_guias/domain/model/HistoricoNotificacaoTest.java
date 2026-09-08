package br.com.astecob.aviso_guias.domain.model;

import br.com.astecob.aviso_guias.domain.enums.CanalNotificacao;
import br.com.astecob.aviso_guias.domain.enums.ResultadoNotificacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HistoricoNotificacaoTest {

    @Test
    void deveCriarHistoricoDeSucesso() {
        HistoricoNotificacao historico = new HistoricoNotificacao(
                UUID.randomUUID(), "Empresa Teste", "teste@example.com", "21999999999",
                CanalNotificacao.EMAIL, ResultadoNotificacao.SUCESSO, null);

        assertNotNull(historico.getId());
        assertNotNull(historico.getDataHora());
        assertNull(historico.getMotivoFalha());
    }

    @Test
    void deveCriarHistoricoDeFalhaComMotivo() {
        HistoricoNotificacao historico = new HistoricoNotificacao(
                UUID.randomUUID(), "Empresa Teste", "teste@example.com", "21999999999",
                CanalNotificacao.WHATSAPP, ResultadoNotificacao.FALHA, "API indisponível");

        assertEquals("API indisponível", historico.getMotivoFalha());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void deveRejeitarFalhaSemMotivo(String motivo) {
        assertThrows(IllegalArgumentException.class, () ->
                new HistoricoNotificacao(UUID.randomUUID(), "Empresa Teste", "teste@example.com", "21999999999",
                        CanalNotificacao.EMAIL, ResultadoNotificacao.FALHA, motivo));
    }

    @Test
    void deveRejeitarSucessoComMotivo() {
        assertThrows(IllegalArgumentException.class, () ->
                new HistoricoNotificacao(UUID.randomUUID(), "Empresa Teste", "teste@example.com", "21999999999",
                        CanalNotificacao.EMAIL, ResultadoNotificacao.SUCESSO, "não deveria ter motivo"));
    }

    @Test
    void deveRejeitarGuiaIdNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new HistoricoNotificacao(null, "Empresa Teste", "teste@example.com", "21999999999",
                        CanalNotificacao.EMAIL, ResultadoNotificacao.SUCESSO, null));
    }
}
