package com.expense.tracker.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.expense.tracker.entity.User;
import com.expense.tracker.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // @Override
    // public UserDetails loadUserByUsername(String username) throws
    // UsernameNotFoundException {
    // User user = userRepository.findByUsername(username)
    // .orElseThrow(() -> new UsernameNotFoundException("User not found with
    // username: " + username));

    // System.out.println("Fetched User: " + user);

    // UserDetails userDetails=
    // org.springframework.security.core.userdetails.User.builder()
    // .username(user.getUsername())
    // .password(user.getPassword())
    // .roles(user.getRole().replace("ROLE_", ""))
    // .build();

    // System.out.println("UserDetails Built: " + userDetails);

    // return userDetails;
    // }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        System.out.println("Fetched User: " + user);

        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            role.getPermissions().forEach((permission) -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();

        System.out.println("UserDetails Built: " + userDetails);

        return userDetails;
    }
}
