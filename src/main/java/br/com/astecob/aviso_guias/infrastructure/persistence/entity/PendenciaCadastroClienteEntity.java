package br.com.astecob.aviso_guias.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pendencia_cadastro_cliente")
public class PendenciaCadastroClienteEntity {

    @Id
    private UUID id;

    @Column(name = "tipo_guia", nullable = false)
    private String tipoGuia;

    @Column(name = "nome_empresa", nullable = false)
    private String nomeEmpresa;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private Integer ano;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    protected PendenciaCadastroClienteEntity() {
    }

    public PendenciaCadastroClienteEntity(UUID id, String tipoGuia, String nomeEmpresa,
                                          Integer mes, Integer ano, Instant criadoEm) {
        this.id = id;
        this.tipoGuia = tipoGuia;
        this.nomeEmpresa = nomeEmpresa;
        this.mes = mes;
        this.ano = ano;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public String getTipoGuia() {
        return tipoGuia;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public Integer getMes() {
        return mes;
    }

    public Integer getAno() {
        return ano;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}