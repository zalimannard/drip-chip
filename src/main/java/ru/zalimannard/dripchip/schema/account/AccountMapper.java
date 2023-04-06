package ru.zalimannard.dripchip.schema.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    Account toEntity(AuthenticationDto dto);

    AccountDto toDto(Account entity);

    List<AccountDto> toDtoList(List<Account> entity);

}
