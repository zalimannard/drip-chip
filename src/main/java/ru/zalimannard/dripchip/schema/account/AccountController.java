package ru.zalimannard.dripchip.schema.account;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

import java.util.List;

@RestController
@RequestMapping("${application.endpoint.accounts}")
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

    @PostConstruct
    private void postConstruct() {
        AccountDto adminAccountDto = AccountDto.builder()
                .firstName("adminFirstName")
                .lastName("adminLastName")
                .email(System.getenv("ADMIN_EMAIL"))
                .password(System.getenv("ADMIN_PASSWORD"))
                .role(AccountRole.ADMIN)
                .build();
        accountService.create(adminAccountDto);

        AccountDto chipperAccountDto = AccountDto.builder()
                .firstName("chipperFirstName")
                .lastName("chipperLastName")
                .email(System.getenv("CHIPPER_EMAIL"))
                .password(System.getenv("CHIPPER_PASSWORD"))
                .role(AccountRole.CHIPPER)
                .build();
        accountService.create(chipperAccountDto);

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
