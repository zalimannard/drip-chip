package ru.zalimannard.dripchip.schema.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AccountMapper {

    Account toEntity(AccountDto dto);

    @Mapping(target = "password", ignore = true)
    AccountDto toDto(Account entity);

    List<Account> toEntityList(List<AccountDto> dto);

    List<AccountDto> toDtoList(List<Account> entity);

}
