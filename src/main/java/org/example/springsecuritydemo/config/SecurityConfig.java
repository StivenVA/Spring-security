package org.example.springsecuritydemo.config;

import lombok.RequiredArgsConstructor;
import org.example.springsecuritydemo.entity.TipoRol;
import org.example.springsecuritydemo.entity.user.CustomUserDetails;
import org.example.springsecuritydemo.services.UserCustomDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserCustomDetailService userCustomDetailService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userCustomDetailService);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("v1/index","v1/session")
                        .hasAuthority("ROLE_LIDER")
                        .requestMatchers("v1/index2")
                        .hasAnyAuthority("ROLE_JUNIOR")
                        .requestMatchers("login.html").permitAll()
                        .requestMatchers("v1/forbidden").denyAll()
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .successHandler(
                                (request, response, authentication) ->{
                                    if (
                                            ((CustomUserDetails) authentication.getPrincipal())
                                            .getAuthorities()
                                            .stream().anyMatch(
                                                    a -> a.getAuthority().equals(
                                                            TipoRol.ROLE_LIDER.name()
                                                    )
                                            )
                                    )
                                        response.sendRedirect("/v1/session");
                                     else
                                        response.sendRedirect("/v1/index2");

                                }
                ))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .expiredUrl("/login")
                )
                .build();
    }

    @Bean
    protected SecurityFilterChain configureExample(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
