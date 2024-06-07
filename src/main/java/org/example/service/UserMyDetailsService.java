package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserMyDetailsService implements UserDetailsService {
    //доступ к репозиторию
    private final UserRepository userRepository;

    @Autowired
    public UserMyDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("cannot find user with this email"));
        return SecurityUser.fromUser(user);
    }
}

