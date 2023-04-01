package ru.zalimannard.dripchip.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    @Value("${application.endpoint.registration}")
    private String registrationPath;
    @Value("${application.endpoint.accounts}")
    private String accountsPath;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .csrf().disable()
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, registrationPath).anonymous()
                        .requestMatchers(HttpMethod.POST, accountsPath).hasAnyAuthority(AccountRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement().disable();
        return http.build();
    }

}