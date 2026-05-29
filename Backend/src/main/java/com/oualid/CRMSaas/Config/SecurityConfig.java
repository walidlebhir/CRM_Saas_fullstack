package com.oualid.CRMSaas.Config;

import com.oualid.CRMSaas.Filters.JWTfilter;
import com.oualid.CRMSaas.Services.AuthentificationService.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTfilter jwtfilter;
    private final CustomUserDetailsService detailUser;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http

                // CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF
                .csrf(csrf -> csrf.disable())

                // Authorization
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/authentication/**").permitAll()
                        .requestMatchers("/webhooks/**").permitAll()
                        .requestMatchers("/payement/system/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                // UserDetailsService
                .userDetailsService(detailUser)

                // JWT Filter
                .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // FRONTEND NEXT.JS
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000"
        ));

        // METHODS
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // HEADERS
        configuration.setAllowedHeaders(List.of("*"));

        // JWT / COOKIES
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }
}