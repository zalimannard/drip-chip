package ru.zalimannard.dripchip.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;

@Component
public class AccountToAuthCode {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String convert(String adminEmail, String adminPassword) {
        String stringToEncode = adminEmail + ":" + adminPassword;
        return passwordEncoder.encode(stringToEncode);
    }

    public String convert(AccountRequestDto accountRequestDto) {
        return convert(accountRequestDto.getEmail(), accountRequestDto.getPassword());
    }

}
