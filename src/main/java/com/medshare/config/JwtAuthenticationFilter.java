package com.medshare.config;

import com.medshare.service.JwtTokenService;
import com.medshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        String jwt = null;
        String username = null;

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7);
            try {
                username = jwtTokenService.extractUsername(jwt);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Fetch user details and validate token
            var userDtoOpt = userService.findByEmail(username);
            if (userDtoOpt.isPresent()) {
                var userDto = userDtoOpt.get();
                // Create UserDetailsImpl with the user's ID
                UserDetails userDetails = new UserDetailsImpl(
                    userDto.getUserId(), // This is the UUID ID
                    userDto.getEmail(),
                    "", // password is not needed for JWT validation
                    Collections.singletonList(new SimpleGrantedAuthority("USER"))
                );

                if (jwtTokenService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        chain.doFilter(request, response);
    }
}