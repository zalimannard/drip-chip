package ru.zalimannard.dripchip.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSecurity {

    private final AccountRepository accountRepository;

    public boolean hasUserId(Authentication authentication, int userId) {
        String authEmail = authentication.getName();
        Optional<Account> accountAuthed = accountRepository.findByEmail(authEmail);
        return accountAuthed.filter(account -> userId == account.getId()).isPresent();
    }

}
