package com.expense.tracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense.tracker.entity.User;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(new HashMap<>(), userDetails.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(new HashMap<>(), userDetails.getUsername());

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            return response;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @PostMapping("/api/auth/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String refreshToken = loginRequest.get("refreshToken");

        // Validate the refresh token
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (!jwtUtil.validateToken(refreshToken, username)) {
            throw new RuntimeException("Refresh token is expired or invalid");
        }
        // Generate a new access token
        String accessToken = jwtUtil.generateAccessToken(new HashMap<>(), user.getUsername());

        // Return the new access token
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);

        return ResponseEntity.ok(tokens);
    }

}
