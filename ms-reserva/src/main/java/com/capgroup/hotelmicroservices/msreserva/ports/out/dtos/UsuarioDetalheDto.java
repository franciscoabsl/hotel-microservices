package com.capgroup.hotelmicroservices.msreserva.ports.out.dtos;


import java.util.UUID;

public record UsuarioDetalheDto(
        UUID id,
        String nome,
        String email,
        String perfil
) {}