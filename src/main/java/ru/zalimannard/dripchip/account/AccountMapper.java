package ru.zalimannard.dripchip.account;

import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {

    Account toEntity(AccountDto dto);

    AccountDto toDto(Account entity);

}
