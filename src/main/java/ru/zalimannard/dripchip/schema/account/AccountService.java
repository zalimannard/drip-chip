package ru.zalimannard.dripchip.schema.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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


    AccountResponseDto read(@Positive @NotNull Integer id);

    Account readEntity(@Positive @NotNull Integer id);


    List<AccountResponseDto> search(String firstName,
                                    String lastName,
                                    String email,
                                    @PositiveOrZero @NotNull Integer from,
                                    @Positive @NotNull Integer size);

    List<Account> searchEntities(String firstName,
                                 String lastName,
                                 String email,
                                 @PositiveOrZero @NotNull Integer from,
                                 @Positive @NotNull Integer size);


    AccountResponseDto update(@Positive @NotNull Integer id, @Valid AccountRequestDto accountRequestDto);

    Account updateEntity(@Positive @NotNull Integer id, Account account);


    void delete(@Positive @NotNull Integer id);

}
