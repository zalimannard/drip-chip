package ru.zalimannard.dripchip.schema.account.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountDto;
import ru.zalimannard.dripchip.schema.account.AccountMapper;
import ru.zalimannard.dripchip.schema.account.AccountService;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

@Service
@RequiredArgsConstructor
@Validated
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @Override
    public AccountDto register(AuthenticationDto authenticationDto) {
        Account accountRequest = accountMapper.toEntity(authenticationDto);
        accountRequest.setRole(AccountRole.USER);

        Account accountResponse = accountService.createEntity(accountRequest);

        return accountMapper.toDto(accountResponse);
    }

}
