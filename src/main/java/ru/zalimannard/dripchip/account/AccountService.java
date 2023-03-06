package ru.zalimannard.dripchip.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public interface AccountService {

    AccountDto create(@Valid AccountDto accountDto);

    AccountDto read(@Positive int id);

    List<AccountDto> search(AccountDto filter, @PositiveOrZero int from, @Positive int size);

    AccountDto update(@Positive int id, @Valid AccountDto accountDto);

    void delete(@Positive int id);

}
