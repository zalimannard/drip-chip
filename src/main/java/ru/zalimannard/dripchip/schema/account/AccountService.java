package ru.zalimannard.dripchip.schema.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import java.util.List;

@Validated
public interface AccountService {

    AccountResponseDto create(@Valid AccountRequestDto accountRequestDto);

    Account createEntity(Account account);


    AccountResponseDto read(@Positive int id);

    Account readEntity(int id);


    List<AccountResponseDto> search(String firstName,
                                    String lastName,
                                    String email,
                                    @PositiveOrZero int from, @Positive int size);

    List<Account> searchEntities(String firstName, String lastName, String email, int from, int size);


    AccountResponseDto update(@Positive int id, @Valid AccountRequestDto accountRequestDto);

    Account updateEntity(int id, Account account);


    void delete(@Positive int id);

}
