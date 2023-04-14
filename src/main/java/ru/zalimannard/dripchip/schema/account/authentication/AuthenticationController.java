package ru.zalimannard.dripchip.schema.account.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

@RestController
@RequestMapping("${application.endpoint.authentication}")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto createAccount(@RequestBody AuthenticationDto authenticationDto) {
        return authenticationService.register(authenticationDto);
    }

}
