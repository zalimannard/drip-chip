package ru.zalimannard.dripchip.schema.account;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.page.OffsetBasedPage;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public AccountResponseDto create(AccountRequestDto accountRequestDto) {
        Account accountRequest = accountMapper.toEntity(accountRequestDto);

        Account accountResponse = createEntity(accountRequest);

        return accountMapper.toDto(accountResponse);
    }

    @Override
    public Account createEntity(Account account) {
        try {
            String textToEncode = account.getEmail() + ":" + account.getPassword();
            String password = encoder.encode(textToEncode);

            account = account.toBuilder()
                    .password(password)
                    .build();
            return repository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("acc-01", "account", e.getLocalizedMessage());
        }
    }

    @Override
    public AccountResponseDto read(int id) {
        Account accountResponse = readEntity(id);

        return accountMapper.toDto(accountResponse);
    }

    @Override
    public Account readEntity(int id) {
        Optional<Account> responseOptional = repository.findById(id);
        if (responseOptional.isPresent()) {
            return responseOptional.get();
        } else {
            throw new NotFoundException("acc-02", "id", String.valueOf(id));
        }
    }

    @Override
    public Account readEntityByEmail(String email) {
        Optional<Account> responseOptional = repository.findByEmail(email);
        if (responseOptional.isPresent()) {
            return responseOptional.get();
        } else {
            throw new NotFoundException("acc-03", "email", String.valueOf(email));
        }
    }

    @Override
    public List<AccountResponseDto> search(AccountRequestDto filterDto, int from, int size) {
        Account filter = accountMapper.toEntity(filterDto);

        List<Account> accounts = searchEntities(filter, from, size);

        return accountMapper.toDtoList(accounts);
    }

    @Override
    public List<Account> searchEntities(Account filter, int from, int size) {
        Pageable pageable = new OffsetBasedPage(from, size);

        return repository.search(
                filter.getLastName(),
                filter.getFirstName(),
                filter.getEmail(),
                pageable
        );
    }

    @Override
    public AccountResponseDto update(int id, AccountRequestDto accountRequestDto) {
        Account accountRequest = accountMapper.toEntity(accountRequestDto);

        Account accountResponse = updateEntity(id, accountRequest);

        return accountMapper.toDto(accountResponse);
    }

    @Override
    public Account updateEntity(int id, Account account) {
        if (repository.existsById(id)) {
            String textToEncode = account.getEmail() + ":" + account.getPassword();
            String password = encoder.encode(textToEncode);

            account = account.toBuilder()
                    .id(id)
                    .password(password)
                    .build();
            return repository.save(account);
        } else {
            throw new NotFoundException("acc-04", "id", String.valueOf(id));
        }
    }

    @Override
    public void delete(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new NotFoundException("acc-05", "id", String.valueOf(id));
        }
    }

}
