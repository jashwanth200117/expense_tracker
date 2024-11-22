// package com.expense.tracker.config;

// import com.expense.tracker.security.JwtAuthenticationFilter;
// import com.expense.tracker.security.JwtUtils;
// import com.expense.tracker.service.CustomUserDetailsService;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// public class SecurityConfig {

//     private final CustomUserDetailsService userDetailsService;
//     private final JwtUtils jwtUtils;
//     private final JwtAuthenticationFilter jwtAuthenticationFilter;

//     public SecurityConfig(CustomUserDetailsService userDetailsService, JwtUtils jwtUtils,
//                           JwtAuthenticationFilter jwtAuthenticationFilter) {
//         this.userDetailsService = userDetailsService;
//         this.jwtUtils = jwtUtils;
//         this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf().disable()
//             .authorizeRequests()
//             .requestMatchers("/api/reports/**").authenticated()
//             .requestMatchers("/api/admin/**").hasRole("ADMIN")
//             .anyRequest().permitAll()
//             .and()
//             .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before authentication filter
//         return http.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//         return http.getSharedObject(AuthenticationManagerBuilder.class)
//                 .userDetailsService(userDetailsService)
//                 .passwordEncoder(passwordEncoder())
//                 .and()
//                 .build();
//     }
// }


package com.expense.tracker.config;

import com.expense.tracker.security.JwtAuthenticationFilter;
import com.expense.tracker.security.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public SecurityConfig(UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .requestMatchers("/api/auth/**").permitAll()  // Allowing authentication endpoints
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(http), jwtUtils));  // Register the filter here
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .userDetailsService(userDetailsService)
                   .passwordEncoder(passwordEncoder())
                   .and()
                   .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
