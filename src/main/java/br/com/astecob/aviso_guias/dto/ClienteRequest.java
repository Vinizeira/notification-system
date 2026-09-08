package br.com.astecob.aviso_guias.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(

        @NotBlank(message = "O nome da empresa é obrigatório.")
        String nomeEmpresa,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail deve ter um formato válido.")
        String email,

        @NotBlank(message = "O telefone/WhatsApp é obrigatório.")
        String telefoneWhatsapp
) {
}