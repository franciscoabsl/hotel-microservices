package com.capgroup.hotelmicroservices.msauthuser.controller;

import com.capgroup.hotelmicroservices.msauthuser.domain.Usuario;
import com.capgroup.hotelmicroservices.msauthuser.dto.UsuarioResponse;
import com.capgroup.hotelmicroservices.msauthuser.dto.UsuarioUpdateRequest;
import com.capgroup.hotelmicroservices.msauthuser.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable UUID id, Authentication authentication) {
        authorizeUserOrAdmin(id, authentication);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return ResponseEntity.ok(toResponse(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable UUID id,
                                                 @RequestBody @Valid UsuarioUpdateRequest req,
                                                 Authentication authentication) {
        authorizeUserOrAdmin(id, authentication);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        // Validações de unicidade se email/cpf forem alterados
        if (!usuario.getEmail().equals(req.email()) && usuarioRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (!usuario.getCpf().equals(req.cpf()) && usuarioRepository.existsByCpf(req.cpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }

        usuario.setNome(req.nome());
        usuario.setEmail(req.email());
        usuario.setCpf(req.cpf());
        usuario.setPerfil(req.perfil());
        usuario.setTelefone(req.telefone());

        Usuario salvo = usuarioRepository.save(usuario);
        return ResponseEntity.ok(toResponse(salvo));
    }

    private void authorizeUserOrAdmin(UUID pathId, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
        }

        String userIdFromHeader = String.valueOf(authentication.getPrincipal());
        boolean isAdmin = AuthorityUtils.authorityListToSet(authentication.getAuthorities()).contains("ROLE_ADMIN");
        if (!isAdmin && !pathId.toString().equals(userIdFromHeader)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getCpf(), u.getPerfil(), u.getTelefone());
    }
}
