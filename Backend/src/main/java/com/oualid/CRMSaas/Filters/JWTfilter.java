package com.oualid.CRMSaas.Filters;
import java.io.IOException;


import com.oualid.CRMSaas.Services.AuthentificationService.CustomUserDetailsService;
import com.oualid.CRMSaas.Services.AuthentificationService.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JWTfilter extends OncePerRequestFilter {

    private final JWTService jwtservice;
    private final CustomUserDetailsService userDetailsService;


    /**
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException      Filter jwt pour filterer request Envoie par User
     *                          validation request
     */


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Si aucun token ou header incorrect, on continue la chaîne de filtres
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // retirer "Bearer "
        String email = jwtservice.extractEmail(token);

        // Si email valide et pas encore authentifié
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Charger UserDetails via CustomUserDetailsService
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Vérifier que le token est valide (optionnel mais recommandé)
            if (jwtservice.isTokenValid(token, userDetails.getUsername())) {

                // Créer l’authentification Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,         // <-- UserDetails comme principal
                                null,
                                userDetails.getAuthorities()
                        );

                // Mettre dans le contexte
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}



