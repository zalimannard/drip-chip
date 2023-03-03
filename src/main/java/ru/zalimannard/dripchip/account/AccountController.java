package ru.zalimannard.dripchip.account;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${application.endpoint.accounts}")
@Validated
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("{id}")
    public AccountDto get(@PathVariable @Min(1) int id) {
        return accountService.read(id);
    }
}
