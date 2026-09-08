package br.com.astecob.aviso_guias.infrastructure.persistence.entity;

import br.com.astecob.aviso_guias.domain.model.Cliente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    private UUID id;

    @Column(name = "nome_empresa", nullable = false)
    private String nomeEmpresa;

    @Column(name = "nome_empresa_normalizado", nullable = false, unique = true)
    private String nomeEmpresaNormalizado;

    @Column(nullable = false)
    private String email;

    @Column(name = "telefone_whatsapp", nullable = false)
    private String telefoneWhatsapp;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    protected ClienteEntity() {
    }

    private ClienteEntity(
            UUID id,
            String nomeEmpresa,
            String nomeEmpresaNormalizado,
            String email,
            String telefoneWhatsapp,
            Instant criadoEm
    ) {
        this.id = id;
        this.nomeEmpresa = nomeEmpresa;
        this.nomeEmpresaNormalizado = nomeEmpresaNormalizado;
        this.email = email;
        this.telefoneWhatsapp = telefoneWhatsapp;
        this.criadoEm = criadoEm;
    }

    public static ClienteEntity fromDomain(Cliente cliente) {
        return new ClienteEntity(
                cliente.getId(),
                cliente.getNomeEmpresa(),
                cliente.getNomeEmpresaNormalizado(),
                cliente.getEmail(),
                cliente.getTelefoneWhatsapp(),
                cliente.getCriadoEm()
        );
    }

    public Cliente toDomain() {
        return Cliente.restaurar(
                id,
                nomeEmpresa,
                email,
                telefoneWhatsapp,
                criadoEm
        );
    }
}