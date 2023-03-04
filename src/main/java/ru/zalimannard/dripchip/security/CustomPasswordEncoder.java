package ru.zalimannard.dripchip.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.account.Account;
import ru.zalimannard.dripchip.account.AccountRepository;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class CustomPasswordEncoder implements PasswordEncoder {

    private final AccountRepository accountRepository;

    @Override
    public String encode(CharSequence rawPassword) {
        return Base64.getEncoder().encodeToString(rawPassword.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        Account account = accountRepository.findByPassword(encodedPassword);
        if (account != null) {
            String anotherEncodedPassword = encode(account.getEmail() + ":" + rawPassword);
            return anotherEncodedPassword.equals(encodedPassword);
        } else {
            return false;
        }
    }

}
