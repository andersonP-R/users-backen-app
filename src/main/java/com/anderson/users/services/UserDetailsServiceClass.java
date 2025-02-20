package com.anderson.users.services;

import com.anderson.users.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceClass implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceClass(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // method to verify our incoming user
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<com.anderson.users.models.User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw  new UsernameNotFoundException(String.format("Username: %s does not exist!", username));
        }

        com.anderson.users.models.User user1 = user.orElseThrow();

        // map all roles in our authorities list
        List<GrantedAuthority> authorities = user1
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return new User(user1.getUsername(),
                user1.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }
}
