package com.capgroup.hotelmicroservices.msauthuser.dto;

import com.capgroup.hotelmicroservices.msauthuser.domain.Perfil;

import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nome,
        String email,
        String cpf,
        Perfil perfil,
        String telefone
) {}
