package com.example.bookbank.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Get Authorization header

        String authHeader = request.getHeader("Authorization");


        // 2. Check whether Authorization header exists

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }


        // 3. Remove "Bearer " from the token

        String token = authHeader.substring(7);


        // 4. Validate JWT

        if (jwtService.isTokenValid(token)) {


            // 5. Extract user ID

            String userId = jwtService.extractUserId(token);


            // 6. Extract role

            String role = jwtService.extractRole(token);


            // 7. Create Authentication object

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + role
                                    )
                            )
                    );


            // 8. Store authentication in SecurityContext

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);


            System.out.println("JWT is valid");
            System.out.println("User ID: " + userId);
            System.out.println("Role: " + role);
        }


        // 9. Continue request

        filterChain.doFilter(request, response);
    }
}