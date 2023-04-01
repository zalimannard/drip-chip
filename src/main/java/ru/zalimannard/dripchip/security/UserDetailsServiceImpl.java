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
import ru.zalimannard.dripchip.schema.account.AccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority(account.getRole().toString()),
                    new SimpleGrantedAuthority(account.getId().toString()));
            return new User(account.getEmail(), account.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

}