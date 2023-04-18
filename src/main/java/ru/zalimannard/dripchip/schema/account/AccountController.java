package ru.zalimannard.dripchip.schema.account;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import java.util.List;

@RestController
@RequestMapping("${application.endpoint.accounts}")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("{id}")
    public AccountResponseDto get(@PathVariable @Positive int id) {
        return accountService.read(id);
    }

    @GetMapping("${application.endpoint.search}")
    public List<AccountResponseDto> search(AccountRequestDto filter,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return accountService.search(filter, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto post(@RequestBody AccountRequestDto accountRequestDto) {
        return accountService.create(accountRequestDto);
    }

    @PutMapping("{id}")
    public AccountResponseDto put(@PathVariable int id,
                                  @RequestBody AccountRequestDto accountRequestDto) {
        return accountService.update(id, accountRequestDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable int id) {
        accountService.delete(id);
    }

}
