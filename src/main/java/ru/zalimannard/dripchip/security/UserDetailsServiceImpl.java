package ru.zalimannard.dripchip.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Account> accounts = accountService.searchEntities(null, null, email, 0, 1);
        if (!accounts.isEmpty()) {
            Account account = accounts.get(0);
            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority(account.getRole().toString()),
                    new SimpleGrantedAuthority(account.getId().toString()));
            return new User(account.getEmail(), account.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("Пользователь с email: " + email + " не найден.");
        }
    }

}