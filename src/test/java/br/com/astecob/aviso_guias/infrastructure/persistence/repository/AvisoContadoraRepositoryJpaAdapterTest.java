package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.model.AvisoContadora;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AvisoContadoraRepositoryJpaAdapter.class)
class AvisoContadoraRepositoryJpaAdapterTest {

    @Autowired
    private AvisoContadoraRepositoryJpaAdapter adapter;

    @Test
    void deveSalvarEListarAvisoSemGuia() {
        AvisoContadora aviso = new AvisoContadora("Erro genérico de processamento", null);

        adapter.salvar(aviso);

        List<AvisoContadora> todos = adapter.listarTodas();

        assertEquals(1, todos.size());
        assertNull(todos.get(0).getGuiaId());
    }
}