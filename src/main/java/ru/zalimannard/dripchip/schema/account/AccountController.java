package ru.zalimannard.dripchip.schema.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import java.util.List;

@RestController
@RequestMapping("${application.endpoint.accounts}")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @GetMapping("{id}")
    public AccountResponseDto get(@PathVariable Integer id) {
        return accountService.read(id);
    }

    @GetMapping("${application.endpoint.search}")
    public List<AccountResponseDto> search(@RequestParam(required = false) String firstName,
                                           @RequestParam(required = false) String lastName,
                                           @RequestParam(required = false) String email,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return accountService.search(firstName, lastName, email, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto post(@RequestBody AccountRequestDto accountRequestDto) {
        return accountService.create(accountRequestDto);
    }

    @PutMapping("{id}")
    public AccountResponseDto put(@PathVariable Integer id,
                                  @RequestBody AccountRequestDto accountRequestDto) {
        return accountService.update(id, accountRequestDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        accountService.delete(id);
    }

}
