package ru.zalimannard.dripchip.integration.registration;

import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

public abstract class RegistrationDefaultDtos {

    public static final AuthenticationDto defaultAuthentication = new AuthenticationDto(
            "Иван",
            "Иванов",
            "ivan@mail.ru",
            "password"
    );

    public static final AccountResponseDto defaultAccountResponse = AccountResponseDto.builder()
            .firstName("Иван")
            .lastName("Иванов")
            .email("ivan@mail.ru")
            .role(AccountRole.USER)
            .build();

}
