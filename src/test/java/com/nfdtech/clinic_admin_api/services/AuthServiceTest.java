package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.user.Role;
import com.nfdtech.clinic_admin_api.domain.user.Usuario;
import com.nfdtech.clinic_admin_api.dto.auth.LoginRequestDTO;
import com.nfdtech.clinic_admin_api.dto.auth.LoginResponseDTO;
import com.nfdtech.clinic_admin_api.dto.auth.RefreshTokenRequestDTO;
import com.nfdtech.clinic_admin_api.exception.custom.UnauthorizedException;
import com.nfdtech.clinic_admin_api.repositories.UsuarioRepository;
import com.nfdtech.clinic_admin_api.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar valor da propriedade jwtExpiration via reflection
        ReflectionTestUtils.setField(authService, "jwtExpiration", 3600000L);

        usuario = Usuario.builder()
                .id(1L)
                .username("admin")
                .email("admin@clinic.com")
                .password("$2a$10$hashedPassword")
                .role(Role.ADMIN)
                .active(true)
                .build();

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("admin");
        loginRequestDTO.setPassword("admin123");
    }

    @Test
    void deveRealizarLoginComSucesso() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("access-token-123");
        when(tokenProvider.generateRefreshToken("admin")).thenReturn("refresh-token-456");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        LoginResponseDTO response = authService.login(loginRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token-123");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token-456");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(3600000L);
        assertThat(response.getUsername()).isEqualTo("admin");
        assertThat(response.getRole()).isEqualTo(Role.ADMIN);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, times(1)).generateToken(authentication);
        verify(tokenProvider, times(1)).generateRefreshToken("admin");
        verify(usuarioRepository, times(1)).findByUsername("admin");
    }

    @Test
    void deveLancarExcecaoQuandoCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequestDTO));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, never()).generateToken(any());
        verify(tokenProvider, never()).generateRefreshToken(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoAposAutenticacao() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("access-token-123");
        when(tokenProvider.generateRefreshToken("admin")).thenReturn("refresh-token-456");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequestDTO));

        verify(usuarioRepository, times(1)).findByUsername("admin");
    }

    @Test
    void deveRenovarTokenComSucesso() {
        RefreshTokenRequestDTO refreshRequest = new RefreshTokenRequestDTO();
        refreshRequest.setRefreshToken("valid-refresh-token");

        when(tokenProvider.validateToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("admin");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn("new-access-token");
        when(tokenProvider.generateRefreshToken("admin")).thenReturn("new-refresh-token");

        LoginResponseDTO response = authService.refreshToken(refreshRequest);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");
        assertThat(response.getUsername()).isEqualTo("admin");
        assertThat(response.getRole()).isEqualTo(Role.ADMIN);

        verify(tokenProvider, times(1)).validateToken("valid-refresh-token");
        verify(tokenProvider, times(1)).getUsernameFromToken("valid-refresh-token");
        verify(usuarioRepository, times(1)).findByUsername("admin");
        verify(tokenProvider, times(1)).generateToken(any(Authentication.class));
        verify(tokenProvider, times(1)).generateRefreshToken("admin");
    }

    @Test
    void deveLancarExcecaoQuandoRefreshTokenInvalido() {
        RefreshTokenRequestDTO refreshRequest = new RefreshTokenRequestDTO();
        refreshRequest.setRefreshToken("invalid-refresh-token");

        when(tokenProvider.validateToken("invalid-refresh-token")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.refreshToken(refreshRequest));

        verify(tokenProvider, times(1)).validateToken("invalid-refresh-token");
        verify(tokenProvider, never()).getUsernameFromToken(any());
        verify(usuarioRepository, never()).findByUsername(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioInativoTentaRenovarToken() {
        RefreshTokenRequestDTO refreshRequest = new RefreshTokenRequestDTO();
        refreshRequest.setRefreshToken("valid-refresh-token");

        Usuario usuarioInativo = Usuario.builder()
                .id(1L)
                .username("admin")
                .email("admin@clinic.com")
                .password("$2a$10$hashedPassword")
                .role(Role.ADMIN)
                .active(false)
                .build();

        when(tokenProvider.validateToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("admin");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuarioInativo));

        assertThrows(UnauthorizedException.class, () -> authService.refreshToken(refreshRequest));

        verify(usuarioRepository, times(1)).findByUsername("admin");
        verify(tokenProvider, never()).generateToken(any(Authentication.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoNoRefreshToken() {
        RefreshTokenRequestDTO refreshRequest = new RefreshTokenRequestDTO();
        refreshRequest.setRefreshToken("valid-refresh-token");

        when(tokenProvider.validateToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("inexistente");
        when(usuarioRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.refreshToken(refreshRequest));

        verify(usuarioRepository, times(1)).findByUsername("inexistente");
        verify(tokenProvider, never()).generateToken(any(Authentication.class));
    }

}
