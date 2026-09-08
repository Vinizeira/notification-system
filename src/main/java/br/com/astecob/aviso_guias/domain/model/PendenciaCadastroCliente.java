package br.com.astecob.aviso_guias.domain.model;

import java.time.Instant;
import java.util.UUID;

public class PendenciaCadastroCliente {

    private final UUID id;
    private final String tipoGuia;
    private final String nomeEmpresa;
    private final Integer mes;
    private final Integer ano;
    private final Instant criadoEm;

    private PendenciaCadastroCliente(UUID id, String tipoGuia, String nomeEmpresa, Integer mes, Integer ano, Instant criadoEm) {
        validar(tipoGuia, nomeEmpresa, mes, ano);
        this.id = id;
        this.tipoGuia = tipoGuia;
        this.nomeEmpresa = nomeEmpresa;
        this.mes = mes;
        this.ano = ano;
        this.criadoEm = criadoEm;
    }

    public PendenciaCadastroCliente(String tipoGuia, String nomeEmpresa, Integer mes, Integer ano) {
        this(UUID.randomUUID(), tipoGuia, nomeEmpresa, mes, ano, Instant.now());
    }

    public static PendenciaCadastroCliente restaurar(UUID id, String tipoGuia, String nomeEmpresa, Integer mes,
                                                     Integer ano, Instant criadoEm) {
        return new PendenciaCadastroCliente(id, tipoGuia, nomeEmpresa, mes, ano, criadoEm);
    }

    private void validar(String tipoGuia, String nomeEmpresa, Integer mes, Integer ano) {
        if (tipoGuia == null || tipoGuia.isBlank()) {
            throw new IllegalArgumentException("Tipo de guia é obrigatório");
        }
        if (nomeEmpresa == null || nomeEmpresa.isBlank()) {
            throw new IllegalArgumentException("Nome da empresa é obrigatório");
        }
        if (mes == null || mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês deve estar entre 01 e 12");
        }
        if (ano == null || ano < 2000) {
            throw new IllegalArgumentException("Ano inválido");
        }
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