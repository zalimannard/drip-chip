package ru.zalimannard.dripchip;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.schema.account.AccountService;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final AccountService accountService;
    private final Environment env;

    @PostConstruct
    private void postConstruct() {
        try {
            AccountRequestDto adminAccountRequestDto = AccountRequestDto.builder()
                    .firstName("adminFirstName")
                    .lastName("adminLastName")
                    .email(env.getProperty("application.init.accounts.admin.email"))
                    .password(env.getProperty("application.init.accounts.admin.password"))
                    .role(AccountRole.ADMIN)
                    .build();
            accountService.create(adminAccountRequestDto);
        } catch (ConflictException e) {
            log.info("Email для Админа по умолчанию уже используется");
        }

        try {
            AccountRequestDto chipperAccountRequestDto = AccountRequestDto.builder()
                    .firstName("chipperFirstName")
                    .lastName("chipperLastName")
                    .email(env.getProperty("application.init.accounts.chipper.email"))
                    .password(env.getProperty("application.init.accounts.chipper.password"))
                    .role(AccountRole.CHIPPER)
                    .build();
            accountService.create(chipperAccountRequestDto);
        } catch (ConflictException e) {
            log.info("Email для Чипировщика по умолчанию уже используется");
        }

        try {
            AccountRequestDto userAccountRequestDto = AccountRequestDto.builder()
                    .firstName("userFirstName")
                    .lastName("userLastName")
                    .email(env.getProperty("application.init.accounts.user.email"))
                    .password(env.getProperty("application.init.accounts.user.password"))
                    .role(AccountRole.USER)
                    .build();
            accountService.create(userAccountRequestDto);
        } catch (ConflictException e) {
            log.info("Email для Пользователя по умолчанию уже используется");
        }
    }

}
