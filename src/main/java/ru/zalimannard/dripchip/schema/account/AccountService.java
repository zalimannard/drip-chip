package ru.zalimannard.dripchip.schema.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface AccountService {

    AccountDto create(@Valid AccountDto accountDto);

    AccountDto read(@Positive int id);

    List<AccountDto> search(AccountDto filterDto, @PositiveOrZero int from, @Positive int size);

    AccountDto update(@Positive int id, @Valid AccountDto accountDto);

    void delete(@Positive int id);

}
