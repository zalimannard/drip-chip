package ru.zalimannard.dripchip.schema.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import java.util.List;

@Validated
public interface AccountService {

    AccountResponseDto create(@Valid AccountRequestDto accountRequestDto);

    Account createEntity(@Valid Account account);


    AccountResponseDto read(@Positive int id);

    Account readEntity(@Positive int id);

    Account readEntityByEmail(@NotBlank String email);


    List<AccountResponseDto> search(AccountRequestDto filterDto, @PositiveOrZero int from, @Positive int size);

    List<Account> searchEntities(Account filter, @PositiveOrZero int from, @Positive int size);


    AccountResponseDto update(@Positive int id, @Valid AccountRequestDto accountRequestDto);

    Account updateEntity(@Positive int id, @Valid Account account);


    void delete(@Positive int id);

}
