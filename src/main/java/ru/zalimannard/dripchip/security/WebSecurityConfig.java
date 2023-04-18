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

import java.util.Optional;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AccountRepository accountRepository;

    @Value("${application.endpoint.search}")
    private String searchPath;
    @Value("${application.endpoint.types}")
    private String typesPath;
    @Value("${application.endpoint.authentication}")
    private String registrationPath;
    @Value("${application.endpoint.accounts}")
    private String accountsPath;
    @Value("${application.endpoint.animals}")
    private String animalsPath;
    @Value("${application.endpoint.locations}")
    private String locationsPath;
    @Value("${application.endpoint.areas}")
    private String areasPath;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, registrationPath).anonymous()

                        .requestMatchers(HttpMethod.GET, accountsPath + "/{accountId}").access(
                                (authentication, object) -> new AuthorizationDecision(
                                        authentication.get().getAuthorities().contains(new SimpleGrantedAuthority(AccountRole.ADMIN.toString()))
                                                || accountRequestsItself(authentication, object)))
                        .requestMatchers(HttpMethod.GET, accountsPath + searchPath).hasAuthority(
                                AccountRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, accountsPath + "/{accountId}").hasAuthority(
                                AccountRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.PUT, accountsPath + "/{accountId}").access(
                                (authentication, object) -> new AuthorizationDecision(
                                        authentication.get().getAuthorities().contains(new SimpleGrantedAuthority(AccountRole.ADMIN.toString()))
                                                || accountRequestsItself(authentication, object)))
                        .requestMatchers(HttpMethod.DELETE, accountsPath + "/{accountId}").access(
                                (authentication, object) -> new AuthorizationDecision(
                                        authentication.get().getAuthorities().contains(new SimpleGrantedAuthority(AccountRole.ADMIN.toString()))
                                                || accountRequestsItself(authentication, object)))

                        .requestMatchers(HttpMethod.GET, locationsPath + "/{pointId}").authenticated()
                        .requestMatchers(HttpMethod.POST, locationsPath).hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.PUT, locationsPath + "/{pointId}").hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.DELETE, locationsPath + "/{pointId}").hasAuthority(
                                AccountRole.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, areasPath + "/{areaId}").authenticated()
                        .requestMatchers(HttpMethod.POST, areasPath).hasAuthority(
                                AccountRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.PUT, areasPath + "/{areaId}").hasAuthority(
                                AccountRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, areasPath + "/{areaId}").hasAuthority(
                                AccountRole.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, animalsPath + typesPath + "/{typeId}").authenticated()
                        .requestMatchers(HttpMethod.POST, animalsPath + typesPath).hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.PUT, animalsPath + typesPath + "/{typeId}").hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.DELETE, animalsPath + typesPath + "/{typeId}").hasAuthority(
                                AccountRole.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, animalsPath + "/{animalId}").authenticated()
                        .requestMatchers(HttpMethod.GET, animalsPath + "/{animalId}" + searchPath).authenticated()
                        .requestMatchers(HttpMethod.POST, animalsPath).hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.PUT, animalsPath + "/{animalId}").hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.DELETE, animalsPath + "/{animalId}").hasAuthority(
                                AccountRole.ADMIN.toString())

                        .requestMatchers(HttpMethod.POST, animalsPath + "/{animalId}" + typesPath + "/{typeId}").hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.PUT, animalsPath + "/{animalId}" + typesPath).hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.DELETE, animalsPath + "/{animalId}" + typesPath + "/{typeId}").hasAuthority(
                                AccountRole.ADMIN.toString())

                        .requestMatchers(HttpMethod.GET, animalsPath + "/{animalId}" + locationsPath).authenticated()
                        .requestMatchers(HttpMethod.POST, animalsPath + "/{animalId}" + locationsPath + "/{pointId}").hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.PUT, animalsPath + "/{animalId}" + locationsPath).hasAnyAuthority(
                                AccountRole.ADMIN.toString(), AccountRole.CHIPPER.toString())
                        .requestMatchers(HttpMethod.DELETE, animalsPath + "/{animalId}" + locationsPath + "/{visitedPointId}").hasAuthority(
                                AccountRole.ADMIN.toString())

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement().disable();
        return http.build();
    }

    private boolean accountRequestsItself(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Optional<Account> requesterAccount = accountRepository.findByEmail(authentication.get().getName());
        if (requesterAccount.isEmpty()) {
            // Аккаунт не существует
            return false;
        }
        String requesterId = requesterAccount.get().getId().toString();
        String existingUserId = object.getVariables().get("accountId");
        return requesterId.equals(existingUserId);
    }

}
