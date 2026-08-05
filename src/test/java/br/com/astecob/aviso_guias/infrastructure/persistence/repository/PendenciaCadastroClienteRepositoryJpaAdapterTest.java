package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.model.PendenciaCadastroCliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PendenciaCadastroClienteRepositoryJpaAdapter.class)
class PendenciaCadastroClienteRepositoryJpaAdapterTest {

    @Autowired
    private PendenciaCadastroClienteRepositoryJpaAdapter adapter;

    @Test
    void deveSalvarEListarPendencia() {
        PendenciaCadastroCliente pendencia = new PendenciaCadastroCliente("DAS", "Empresa Sem Cadastro", 6, 2026);

        adapter.salvar(pendencia);

        List<PendenciaCadastroCliente> todas = adapter.listarTodas();

        assertEquals(1, todas.size());
        assertEquals("Empresa Sem Cadastro", todas.get(0).getNomeEmpresa());
    }
}