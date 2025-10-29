package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.user.Usuario;
import com.nfdtech.clinic_admin_api.dto.auth.LoginRequestDTO;
import com.nfdtech.clinic_admin_api.dto.auth.LoginResponseDTO;
import com.nfdtech.clinic_admin_api.dto.auth.RefreshTokenRequestDTO;
import com.nfdtech.clinic_admin_api.exception.custom.UnauthorizedException;
import com.nfdtech.clinic_admin_api.repositories.UsuarioRepository;
import com.nfdtech.clinic_admin_api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            String accessToken = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(loginRequest.getUsername());

            Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));

            return LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtExpiration)
                    .username(usuario.getUsername())
                    .role(usuario.getRole())
                    .build();

        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Credenciais inválidas");
        }
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Refresh token inválido ou expirado");
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));

        if (!usuario.isActive()) {
            throw new UnauthorizedException("Usuário inativo");
        }

        // Criar nova autenticação para gerar novo token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            username, null, Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole().name()))
        );

        String newAccessToken = tokenProvider.generateToken(authentication);
        String newRefreshToken = tokenProvider.generateRefreshToken(username);

        return LoginResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .username(usuario.getUsername())
                .role(usuario.getRole())
                .build();
    }
}
