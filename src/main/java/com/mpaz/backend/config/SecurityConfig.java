package com.mpaz.backend.config;

import com.mpaz.backend.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Acceso publico: login y registro
                        .requestMatchers("/auth/**").permitAll()

                        // Estudiante ve el contenido autorizado y registra sus intentos
                        .requestMatchers(HttpMethod.GET, "/contenidos/autorizados").hasRole("ESTUDIANTE")
                        .requestMatchers(HttpMethod.POST, "/intentos").hasRole("ESTUDIANTE")
                        .requestMatchers(HttpMethod.GET, "/intentos/mios/**").hasRole("ESTUDIANTE")

                        // Docente crea y gestiona su contenido, define intentos, ve informe
                        .requestMatchers(HttpMethod.POST, "/contenidos").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/contenidos/mios").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.PATCH, "/contenidos/*/max-intentos").hasRole("DOCENTE")
                        .requestMatchers("/informe/**").hasRole("DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/intentos/contenido/**").hasRole("DOCENTE")

                        // UTP autoriza o rechaza contenido
                        .requestMatchers(HttpMethod.GET, "/contenidos/pendientes").hasRole("UTP")
                        .requestMatchers(HttpMethod.PATCH, "/contenidos/*/estado").hasRole("UTP")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
