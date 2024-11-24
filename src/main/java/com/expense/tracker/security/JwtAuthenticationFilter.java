// package com.expense.tracker.security;

// import jakarta.servlet.Filter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.FilterConfig;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import java.io.IOException;

// public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

//     private final AuthenticationManager authenticationManager;
//     private final JwtUtils jwtUtils;

//     public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
//         this.authenticationManager = authenticationManager;
//         this.jwtUtils = jwtUtils;
//     }

//     @Override
//     public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
//         String username = request.getParameter("username");
//         String password = request.getParameter("password");

//         // Create an authentication token based on username and password
//         UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

//         // Authenticate the token with the authentication manager
//         return authenticationManager.authenticate(authenticationToken);
//     }

//     @Override
//     protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                             FilterChain chain, Authentication authResult) throws IOException, ServletException {
//         // Extract username from Authentication object
//         String username = authResult.getName();
    
//         // Generate a JWT token using the username
//         String token = jwtUtils.generateJwtToken(username);
    
//         // Add the token to the response header
//         response.addHeader("Authorization", "Bearer " + token);
    
//         // Store the authentication result in the security context
//         SecurityContextHolder.getContext().setAuthentication(authResult);
    
//         // Proceed with the filter chain
//         chain.doFilter(request, response);
//     }
    

//     @Override
//     public void destroy() {
//         // Custom cleanup, if needed.
//     }
// }

package com.expense.tracker.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                logger.warn("JWT token extraction failed: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}

