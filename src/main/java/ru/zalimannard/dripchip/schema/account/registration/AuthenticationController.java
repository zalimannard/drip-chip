package ru.zalimannard.dripchip.schema.account.registration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.account.AccountDto;
import ru.zalimannard.dripchip.schema.account.AccountService;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

@RestController
@RequestMapping("${application.endpoint.authentication}")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        accountDto.setRole(AccountRole.USER);
        return accountService.create(accountDto);
    }

    @PostConstruct
    private void postConstruct() {
        AccountDto adminAccountDto = AccountDto.builder()
                .firstName("adminFirstName")
                .lastName("adminLastName")
                .email(System.getenv("ADMIN_EMAIL"))
                .password(System.getenv("ADMIN_PASSWORD"))
                .role(AccountRole.ADMIN)
                .build();
        int adminAccountId = accountService.create(adminAccountDto).getId();
        accountService.update(adminAccountId, adminAccountDto);

        AccountDto chipperAccountDto = AccountDto.builder()
                .firstName("chipperFirstName")
                .lastName("chipperLastName")
                .email(System.getenv("CHIPPER_EMAIL"))
                .password(System.getenv("CHIPPER_PASSWORD"))
                .role(AccountRole.CHIPPER)
                .build();
        int chipperAccountId = accountService.create(chipperAccountDto).getId();
        accountService.update(chipperAccountId, chipperAccountDto);

        AccountDto userAccountDto = AccountDto.builder()
                .firstName("userFirstName")
                .lastName("userLastName")
                .email(System.getenv("USER_EMAIL"))
                .password(System.getenv("USER_PASSWORD"))
                .role(AccountRole.USER)
                .build();
        accountService.create(userAccountDto);
    }

}
