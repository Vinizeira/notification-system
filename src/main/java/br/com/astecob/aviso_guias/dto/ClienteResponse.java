package br.com.astecob.aviso_guias.dto;

import br.com.astecob.aviso_guias.domain.model.Cliente;

import java.time.Instant;
import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String nomeEmpresa,
        String email,
        String telefoneWhatsapp,
        Instant criadoEm
) {

    public static ClienteResponse fromDomain(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNomeEmpresa(),
                cliente.getEmail(),
                cliente.getTelefoneWhatsapp(),
                cliente.getCriadoEm()
        );
    }
}