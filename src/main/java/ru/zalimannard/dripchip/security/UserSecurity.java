package ru.zalimannard.dripchip.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountRepository;

@Component
@RequiredArgsConstructor
public class UserSecurity {

    private final AccountRepository accountRepository;

    public boolean hasUserId(Authentication authentication, int userId) {
        String authEmail = authentication.getName();
        Account accountAuthed = accountRepository.findByEmail(authEmail);
        return userId == accountAuthed.getId();
    }

}
