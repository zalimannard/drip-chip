package ru.zalimannard.dripchip.schema.account.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountMapper;
import ru.zalimannard.dripchip.schema.account.AccountRepository;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

@Service
@RequiredArgsConstructor
@Validated
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountMapper mapper;
    private final AccountRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public AccountResponseDto register(AuthenticationDto authenticationDto) {
        Account account = mapper.toEntity(authenticationDto);
        Account createdAccount = registerEntity(account);
        return mapper.toDto(createdAccount);
    }

    public Account registerEntity(Account account) {
        try {
            String password = encodePassword(account.getEmail(), account.getPassword());
            account = account.toBuilder()
                    .password(password)
                    .role(AccountRole.USER)
                    .build();
            return repository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("reg-01", "account", e.getLocalizedMessage());
        }
    }

    private String encodePassword(String email, String password) {
        String textToEncode = email + ":" + password;
        return encoder.encode(textToEncode);
    }

}
