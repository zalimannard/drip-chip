package ru.zalimannard.dripchip.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountRepository;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AccountRepository accountRepository;

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
                        .requestMatchers(HttpMethod.PUT, accountsPath + "/{accountId}").access(this::checkAdminOrElementBelongsToUser)
                        .requestMatchers(HttpMethod.DELETE, accountsPath + "/{accountId}").access(this::checkAdminOrElementBelongsToUser)
                        .requestMatchers(HttpMethod.GET, accountsPath + "/{accountId}").access(this::checkAdminOrElementBelongsToUser)
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement().disable();
        return http.build();
    }

    private AuthorizationDecision checkAdminOrElementBelongsToUser(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        if (authentication.get().getAuthorities().contains(new SimpleGrantedAuthority(AccountRole.ADMIN.toString()))) {
            // Admin
            return new AuthorizationDecision(true);
        }

        Account requesterrAccount = accountRepository.findByEmail(authentication.get().getName());
        if (requesterrAccount == null) {
            // Account does not exist
            return new AuthorizationDecision(false);
        }

        String requesterId = requesterrAccount.getId().toString();
        String existingUserId = object.getVariables().get("accountId");
        boolean hasAccess = requesterId.equals(existingUserId);
        return new AuthorizationDecision(hasAccess);
    }

}
