package ru.zalimannard.dripchip.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountMapper {

    Account toEntity(AccountDto dto);

    @Mapping(target = "password", ignore = true)
    AccountDto toDto(Account entity);

}
