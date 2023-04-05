package ru.zalimannard.dripchip.schema.account.authentication;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.account.AccountDto;

@Validated
public interface AuthenticationService {

    AccountDto register(@Valid AuthenticationDto authenticationDto);

}
