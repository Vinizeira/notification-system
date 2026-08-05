package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.enums.CanalNotificacao;
import br.com.astecob.aviso_guias.domain.enums.ResultadoNotificacao;
import br.com.astecob.aviso_guias.domain.model.HistoricoNotificacao;
import br.com.astecob.aviso_guias.infrastructure.persistence.entity.GuiaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(HistoricoNotificacaoRepositoryJpaAdapter.class)
class HistoricoNotificacaoRepositoryJpaAdapterTest {

    @Autowired
    private HistoricoNotificacaoRepositoryJpaAdapter adapter;

    @Autowired
    private TestEntityManager entityManager;

    private UUID criarGuiaDeTeste() {
        UUID guiaId = UUID.randomUUID();
        GuiaEntity guiaEntity = new GuiaEntity(guiaId, "FGTS", "EMPRESA HISTORICO", 6, 2026,
                LocalDate.of(2026, 6, 20), null, "CONCLUIDA", Instant.now());
        entityManager.persist(guiaEntity);
        entityManager.flush();
        return guiaId;
    }

    @Test
    void deveSalvarHistoricoDeSucesso() {
        UUID guiaId = criarGuiaDeTeste();
        HistoricoNotificacao historico = new HistoricoNotificacao(
                guiaId, CanalNotificacao.EMAIL, ResultadoNotificacao.SUCESSO, null);

        adapter.salvar(historico);

        List<HistoricoNotificacao> resultado = adapter.buscarPorGuiaId(guiaId);

        assertEquals(1, resultado.size());
        assertEquals(ResultadoNotificacao.SUCESSO, resultado.get(0).getResultado());
    }

    @Test
    void deveSalvarHistoricoDeFalhaComMotivo() {
        UUID guiaId = criarGuiaDeTeste();
        HistoricoNotificacao historico = new HistoricoNotificacao(
                guiaId, CanalNotificacao.WHATSAPP, ResultadoNotificacao.FALHA, "API indisponível");

        adapter.salvar(historico);

        List<HistoricoNotificacao> resultado = adapter.buscarPorGuiaId(guiaId);

        assertEquals("API indisponível", resultado.get(0).getMotivoFalha());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverHistorico() {
        List<HistoricoNotificacao> resultado = adapter.buscarPorGuiaId(UUID.randomUUID());

        assertTrue(resultado.isEmpty());
    }
}