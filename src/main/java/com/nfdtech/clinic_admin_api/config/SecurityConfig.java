package com.nfdtech.clinic_admin_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // Constantes para endpoints
    private static final String AUTH_ENDPOINT = "/auth/**";
    private static final String SWAGGER_UI_ENDPOINT = "/swagger-ui/**";
    private static final String API_DOCS_ENDPOINT = "/api-docs/**";
    private static final String ACTUATOR_ENDPOINT = "/actuator/**";
    private static final String USUARIOS_ENDPOINT = "/usuarios/**";

    // Constantes para roles/authorities
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_RECEPCAO = "RECEPCAO";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers(AUTH_ENDPOINT).permitAll()
                        .requestMatchers(SWAGGER_UI_ENDPOINT, API_DOCS_ENDPOINT).permitAll()
                        .requestMatchers(ACTUATOR_ENDPOINT).permitAll()

                        // Endpoints protegidos - Usuários
                        .requestMatchers(HttpMethod.GET, USUARIOS_ENDPOINT).hasAnyAuthority(ROLE_ADMIN, ROLE_RECEPCAO)
                        .requestMatchers(HttpMethod.POST, USUARIOS_ENDPOINT).hasAuthority(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PUT, USUARIOS_ENDPOINT).hasAuthority(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, USUARIOS_ENDPOINT).hasAuthority(ROLE_ADMIN)

                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
