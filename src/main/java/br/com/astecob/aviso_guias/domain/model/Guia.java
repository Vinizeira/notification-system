package br.com.astecob.aviso_guias.domain.model;

import br.com.astecob.aviso_guias.domain.enums.StatusGuia;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class Guia {

    private final UUID id;
    private final String tipoGuia;
    private final String nomeEmpresaNormalizado;
    private final Integer mes;
    private final Integer ano;
    private final LocalDate vencimento;
    private UUID clienteId;
    private StatusGuia status;
    private final Instant criadoEm;

    private Guia(UUID id, String tipoGuia, String nomeEmpresaNormalizado, Integer mes, Integer ano,
                 LocalDate vencimento, UUID clienteId, StatusGuia status, Instant criadoEm) {
        validar(tipoGuia, nomeEmpresaNormalizado, mes, ano, vencimento, status);
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

    public Guia(String tipoGuia, String nomeEmpresaNormalizado, Integer mes, Integer ano,
                LocalDate vencimento, UUID clienteId, StatusGuia status) {
        this(UUID.randomUUID(), tipoGuia, nomeEmpresaNormalizado, mes, ano, vencimento, clienteId, status, Instant.now());
    }

    public static Guia restaurar(UUID id, String tipoGuia, String nomeEmpresaNormalizado, Integer mes,
                                 Integer ano, LocalDate vencimento, UUID clienteId, StatusGuia status,
                                 Instant criadoEm) {
        return new Guia(id, tipoGuia, nomeEmpresaNormalizado, mes, ano, vencimento, clienteId, status, criadoEm);
    }

    private void validar(String tipoGuia, String nomeEmpresaNormalizado, Integer mes,
                         Integer ano, LocalDate vencimento, StatusGuia status) {
        if (tipoGuia == null || tipoGuia.isBlank()) {
            throw new IllegalArgumentException("Tipo de guia é obrigatório");
        }
        if (nomeEmpresaNormalizado == null || nomeEmpresaNormalizado.isBlank()) {
            throw new IllegalArgumentException("Nome da empresa normalizado é obrigatório");
        }
        if (mes == null || mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês deve estar entre 01 e 12");
        }
        if (ano == null || ano < 2000) {
            throw new IllegalArgumentException("Ano inválido");
        }
        if (vencimento == null) {
            throw new IllegalArgumentException("Vencimento é obrigatório");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
    }

    public void atualizarStatus(StatusGuia novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
        this.status = novoStatus;
    }

    public void vincularCliente(UUID clienteId) {
        if (clienteId == null) {
            throw new IllegalArgumentException("clienteId é obrigatório para vincular");
        }
        this.clienteId = clienteId;
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

    public StatusGuia getStatus() {
        return status;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}