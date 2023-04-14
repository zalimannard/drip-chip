package ru.zalimannard.dripchip.integration.account;

import com.github.javafaker.Faker;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

public class AccountFactory {

    public static AccountRequestDto createAccountRequest(AccountRole role) {
        return AccountRequestDto.builder()
                .firstName(Faker.instance().name().firstName())
                .lastName(Faker.instance().name().lastName())
                .email(Faker.instance().internet().safeEmailAddress())
                .password(Faker.instance().internet().password())
                .role(role)
                .build();
    }

    public static AccountResponseDto createAccountResponse(Integer id, AccountRequestDto accountRequestDto) {
        return AccountResponseDto.builder()
                .id(id)
                .firstName(accountRequestDto.getFirstName())
                .lastName(accountRequestDto.getLastName())
                .email(accountRequestDto.getEmail())
                .role(accountRequestDto.getRole())
                .build();
    }

}
