package com.capgroup.hotelmicroservices.msreserva.secutiry;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe que representa o usuário no contexto de segurança,
 * com dados extraídos diretamente dos headers (X-User-ID e X-User-Roles).
 */
public class UserDetailsImpl implements UserDetails {

    private final UUID userId;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UUID userId, String rolesHeader) {
        this.userId = userId;
        this.authorities = parseRoles(rolesHeader);
    }

    private List<SimpleGrantedAuthority> parseRoles(String rolesHeader) {

        return List.of(rolesHeader.split(","))
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim().toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public String getPassword() { return null; }

    @Override
    public String getUsername() { return userId.toString(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
