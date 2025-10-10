package com.capgroup.hotelmicroservices.msauthuser.dto;

import com.capgroup.hotelmicroservices.msauthuser.domain.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres") String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 160, message = "E-mail deve ter no máximo 160 caracteres") String email,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter 11 dígitos numéricos") String cpf,

        @NotNull(message = "Perfil é obrigatório") Perfil perfil,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres") String telefone
) {}
