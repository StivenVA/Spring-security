package org.example.springsecuritydemo.services;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritydemo.entity.user.CustomUserDetails;
import org.example.springsecuritydemo.entity.user.UserEntity;
import org.example.springsecuritydemo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCustomDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new CustomUserDetails(userEntity);
    }
}
