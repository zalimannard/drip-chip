package ru.zalimannard.dripchip.schema.account;

import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import java.util.List;

@Component
public interface AccountMapper {

    Account toEntity(AccountRequestDto dto);

    Account toEntity(AuthenticationDto dto);

    AccountResponseDto toDto(Account entity);

    List<AccountResponseDto> toDtoList(List<Account> entity);

}
