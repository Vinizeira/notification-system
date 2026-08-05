package br.com.astecob.aviso_guias.domain.model;

import java.text.Normalizer;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class Cliente {

    private final UUID id;
    private final String nomeEmpresa;
    private final String nomeEmpresaNormalizado;
    private final String email;
    private final String telefoneWhatsapp;
    private final Instant criadoEm;

    private Cliente(
            UUID id,
            String nomeEmpresa,
            String email,
            String telefoneWhatsapp,
            Instant criadoEm
    ) {
        this.id = Objects.requireNonNull(id, "O id do cliente é obrigatório.");
        this.nomeEmpresa = validarTexto(nomeEmpresa, "O nome da empresa é obrigatório.");
        this.nomeEmpresaNormalizado = normalizarNome(nomeEmpresa);
        this.email = validarTexto(email, "O e-mail é obrigatório.");
        this.telefoneWhatsapp = validarTexto(
                telefoneWhatsapp,
                "O telefone/WhatsApp é obrigatório."
        );
        this.criadoEm = Objects.requireNonNull(
                criadoEm,
                "A data de criação é obrigatória."
        );
    }

    public static Cliente novo(
            String nomeEmpresa,
            String email,
            String telefoneWhatsapp
    ) {
        return new Cliente(
                UUID.randomUUID(),
                nomeEmpresa,
                email,
                telefoneWhatsapp,
                Instant.now()
        );
    }

    public static Cliente restaurar(
            UUID id,
            String nomeEmpresa,
            String email,
            String telefoneWhatsapp,
            Instant criadoEm
    ) {
        return new Cliente(
                id,
                nomeEmpresa,
                email,
                telefoneWhatsapp,
                criadoEm
        );
    }

    public UUID getId() {
        return id;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public String getNomeEmpresaNormalizado() {
        return nomeEmpresaNormalizado;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefoneWhatsapp() {
        return telefoneWhatsapp;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    private static String validarTexto(String valor, String mensagemErro) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagemErro);
        }

        return valor.trim();
    }

    public static String normalizarNome(String nomeEmpresa) {
        String nomeValidado = validarTexto(
                nomeEmpresa,
                "O nome da empresa é obrigatório."
        );

        String semAcentos = Normalizer.normalize(
                nomeValidado,
                Normalizer.Form.NFD
        ).replaceAll("\\p{M}", "");

        return semAcentos
                .replaceAll("\\s+", " ")
                .toUpperCase(Locale.ROOT);
    }
}