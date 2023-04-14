package ru.zalimannard.dripchip.integration.account;

import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

public class AccountDefaultDtos {

    public static final AccountRequestDto defaultAccountRequest = AccountRequestDto.builder()
            .firstName("Иван")
            .lastName("Иванов")
            .email("ivan@mail.ru")
            .password("password")
            .role(AccountRole.USER)
            .build();

    public static final AccountResponseDto defaultAccountResponse = AccountResponseDto.builder()
            .firstName("Иван")
            .lastName("Иванов")
            .email("ivan@mail.ru")
            .role(AccountRole.USER)
            .build();

}
