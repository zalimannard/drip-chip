package ru.zalimannard.dripchip.schema.account;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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
    public AccountDto get(@PathVariable @Positive int id) {
        return accountService.read(id);
    }

    @GetMapping("${application.endpoint.search}")
    public List<AccountDto> search(AccountDto filter,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size) {
        return accountService.search(filter, from, size);
    }

    @PutMapping("{id}")
    public AccountDto put(@PathVariable @Positive int id,
                          @RequestBody AccountDto accountDto) {
        return accountService.update(id, accountDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable @Positive int id) {
        accountService.delete(id);
    }

}
