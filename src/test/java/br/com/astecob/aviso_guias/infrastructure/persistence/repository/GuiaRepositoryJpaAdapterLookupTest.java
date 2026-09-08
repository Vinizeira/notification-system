package br.com.astecob.aviso_guias.infrastructure.persistence.repository;

import br.com.astecob.aviso_guias.domain.enums.StatusGuia;
import br.com.astecob.aviso_guias.infrastructure.persistence.entity.GuiaEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuiaRepositoryJpaAdapterLookupTest {
    private final SpringDataGuiaRepository repository = mock(SpringDataGuiaRepository.class);
    private final GuiaRepositoryJpaAdapter adapter = new GuiaRepositoryJpaAdapter(repository);

    @Test
    void deveEncontrarPorIdPreservandoDadosPersistidos() {
        UUID id = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        Instant criadoEm = Instant.parse("2026-06-01T12:00:00Z");
        LocalDate vencimento = LocalDate.of(2026, 6, 20);
        when(repository.findById(id)).thenReturn(Optional.of(new GuiaEntity(id, "FGTS",
                "EMPRESA EXEMPLO", 6, 2026, vencimento, clienteId, "FALHA_EMAIL", criadoEm)));

        var guia = adapter.encontrarPorId(id).orElseThrow();

        assertEquals(id, guia.getId());
        assertEquals(clienteId, guia.getClienteId());
        assertEquals(criadoEm, guia.getCriadoEm());
        assertEquals(vencimento, guia.getVencimento());
        assertEquals(StatusGuia.FALHA_EMAIL, guia.getStatus());
        assertEquals("EMPRESA EXEMPLO", guia.getNomeEmpresaNormalizado());
    }

    @Test
    void deveRetornarVazioParaIdInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertTrue(adapter.encontrarPorId(id).isEmpty());
        verify(repository).findById(id);
    }
}
