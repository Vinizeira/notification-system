package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.enums.StatusGuia;
import br.com.astecob.aviso_guias.domain.model.Guia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(GuiaRepositoryJpaAdapter.class)
class GuiaRepositoryJpaAdapterTest {

    @Autowired
    private GuiaRepositoryJpaAdapter adapter;

    @Test
    void deveSalvarEBuscarPorChaveNatural() {
        Guia guia = new Guia("FGTS", "ASTECOB", 6, 2026,
                LocalDate.of(2026, 6, 20), null, StatusGuia.PENDENTE_CLIENTE);

        adapter.salvar(guia);

        Optional<Guia> encontrada = adapter.buscarPorChaveNatural("FGTS", "ASTECOB", 6, 2026);

        assertTrue(encontrada.isPresent());
        assertEquals(guia.getId(), encontrada.get().getId());
        assertEquals(StatusGuia.PENDENTE_CLIENTE, encontrada.get().getStatus());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarChaveNatural() {
        Optional<Guia> encontrada = adapter.buscarPorChaveNatural("INSS", "INEXISTENTE", 1, 2026);

        assertTrue(encontrada.isEmpty());
    }

    @Test
    void deveAtualizarStatusAoSalvarNovamente() {
        Guia guia = new Guia("DAS", "TESTE ATUALIZACAO", 7, 2026,
                LocalDate.of(2026, 7, 20), null, StatusGuia.PENDENTE_CLIENTE);
        adapter.salvar(guia);

        guia.atualizarStatus(StatusGuia.CONCLUIDA);
        adapter.salvar(guia);

        Optional<Guia> encontrada = adapter.buscarPorChaveNatural("DAS", "TESTE ATUALIZACAO", 7, 2026);

        assertTrue(encontrada.isPresent());
        assertEquals(StatusGuia.CONCLUIDA, encontrada.get().getStatus());
    }
}