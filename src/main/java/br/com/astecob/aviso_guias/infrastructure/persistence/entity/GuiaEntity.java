package br.com.astecob.aviso_guias.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "guias")
public class GuiaEntity {

    @Id
    private UUID id;

    @Column(name = "tipo_guia", nullable = false)
    private String tipoGuia;

    @Column(name = "nome_empresa_normalizado", nullable = false)
    private String nomeEmpresaNormalizado;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false)
    private LocalDate vencimento;

    @Column(name = "cliente_id")
    private UUID clienteId;

    @Column(nullable = false)
    private String status;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    protected GuiaEntity() {
    }

    public GuiaEntity(UUID id, String tipoGuia, String nomeEmpresaNormalizado,
                      Integer mes, Integer ano, LocalDate vencimento,
                      UUID clienteId, String status, Instant criadoEm) {
        this.id = id;
        this.tipoGuia = tipoGuia;
        this.nomeEmpresaNormalizado = nomeEmpresaNormalizado;
        this.mes = mes;
        this.ano = ano;
        this.vencimento = vencimento;
        this.clienteId = clienteId;
        this.status = status;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public String getTipoGuia() {
        return tipoGuia;
    }

    public String getNomeEmpresaNormalizado() {
        return nomeEmpresaNormalizado;
    }

    public Integer getMes() {
        return mes;
    }

    public Integer getAno() {
        return ano;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}