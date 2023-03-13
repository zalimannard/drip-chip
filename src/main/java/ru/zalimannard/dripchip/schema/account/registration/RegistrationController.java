package ru.zalimannard.dripchip.schema.account.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.account.AccountDto;
import ru.zalimannard.dripchip.schema.account.AccountService;

@RestController
@RequestMapping("${application.endpoint.registration}")
@Validated
@RequiredArgsConstructor
public class RegistrationController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return accountService.create(accountDto);
    }

}
