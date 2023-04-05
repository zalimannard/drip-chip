package ru.zalimannard.dripchip.schema.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface AccountService {

    AccountDto create(@Valid AccountDto accountDto);

    Account createEntity(@Valid Account account);


    AccountDto read(@Positive int id);

    Account readEntity(@Positive int id);

    Account readEntityByEmail(@NotBlank String email);


    List<AccountDto> search(AccountDto filterDto, @PositiveOrZero int from, @Positive int size);

    List<Account> searchEntities(Account filter, @PositiveOrZero int from, @Positive int size);


    AccountDto update(@Positive int id, @Valid AccountDto accountDto);

    Account updateEntity(@Positive int id, @Valid Account account);


    void delete(@Positive int id);

}
