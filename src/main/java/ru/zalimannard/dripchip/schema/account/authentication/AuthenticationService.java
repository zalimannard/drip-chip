package ru.zalimannard.dripchip.schema.account.authentication;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

@Validated
public interface AuthenticationService {

    AccountResponseDto register(@Valid AuthenticationDto authenticationDto);

}
