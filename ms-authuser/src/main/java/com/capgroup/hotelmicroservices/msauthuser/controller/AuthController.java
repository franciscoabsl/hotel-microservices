package com.capgroup.hotelmicroservices.msauthuser.controller;

import com.capgroup.hotelmicroservices.msauthuser.domain.Usuario;
import com.capgroup.hotelmicroservices.msauthuser.dto.*;
import com.capgroup.hotelmicroservices.msauthuser.repository.UsuarioRepository;
import com.capgroup.hotelmicroservices.msauthuser.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(@RequestBody @Valid AuthRegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (usuarioRepository.existsByCpf(req.cpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }

        Usuario novo = Usuario.builder()
                .nome(req.nome())
                .email(req.email())
                .cpf(req.cpf())
                .senha(passwordEncoder.encode(req.senha()))
                .perfil(req.perfil())
                .telefone(req.telefone())
                .build();
        Usuario salvo = usuarioRepository.save(novo);

        UsuarioResponse body = new UsuarioResponse(
                salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getCpf(), salvo.getPerfil(), salvo.getTelefone()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid AuthLoginRequest req) {
        var usuario = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (!passwordEncoder.matches(req.senha(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        String token = jwtService.generateToken(usuario);
        return ResponseEntity.ok(AuthTokenResponse.bearer(token, jwtService.getExpirationSeconds()));
    }
}
