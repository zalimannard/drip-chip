package ru.zalimannard.dripchip.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountRepository;

import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Base64PasswordEncoder implements PasswordEncoder {

    private final AccountRepository accountRepository;

    @Override
    public String encode(CharSequence rawPassword) {
        return Base64.getEncoder().encodeToString(rawPassword.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        Optional<Account> account = accountRepository.findByPassword(encodedPassword);
        if (account.isPresent()) {
            String anotherEncodedPassword = encode(account.get().getEmail() + ":" + rawPassword);
            return anotherEncodedPassword.equals(encodedPassword);
        } else {
            return false;
        }
    }

}
