package ru.zalimannard.dripchip.schema.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${application.endpoint.accounts}")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("{id}")
    public AccountDto get(@PathVariable int id) {
        return accountService.read(id);
    }

    @GetMapping("${application.endpoint.search}")
    public List<AccountDto> search(AccountDto filter,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size) {
        return accountService.search(filter, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto post(@RequestBody AccountDto accountDto) {
        return accountService.create(accountDto);
    }

    @PutMapping("{id}")
    public AccountDto put(@PathVariable int id,
                          @RequestBody AccountDto accountDto) {
        return accountService.update(id, accountDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable int id) {
        accountService.delete(id);
    }

}
