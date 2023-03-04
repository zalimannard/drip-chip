package ru.zalimannard.dripchip.account;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("${application.endpoint.search}")
    public List<AccountDto> search(@QuerydslPredicate AccountDto filter,
                                   @RequestParam(defaultValue = "0") @Min(0) int from,
                                   @RequestParam(defaultValue = "10") @Min(1) int size) {
        return accountService.search(filter, from, size);
    }

}
