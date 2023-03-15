package ru.zalimannard.dripchip.schema.account;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountDto dto);

    AccountDto toDto(Account entity);

    List<AccountDto> toDtoList(List<Account> entity);

}
