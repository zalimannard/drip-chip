package ru.zalimannard.dripchip.schema.account;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.page.OffsetBasedPage;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper mapper;
    private final AccountRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public AccountResponseDto create(AccountRequestDto accountRequestDto) {
        Account account = mapper.toEntity(accountRequestDto);
        Account createdAccount = createEntity(account);
        return mapper.toDto(createdAccount);
    }

    @Override
    public Account createEntity(Account account) {
        try {
            String password = encodePassword(account.getEmail(), account.getPassword());
            account = account.toBuilder()
                    .password(password)
                    .build();
            return repository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("acc-01", "account", e.getLocalizedMessage());
        }
    }

    @Override
    public AccountResponseDto read(Integer id) {
        Account readAccount = readEntity(id);
        return mapper.toDto(readAccount);
    }

    @Override
    public Account readEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("acc-02", "id", String.valueOf(id)));
    }

    @Override
    public List<AccountResponseDto> search(String firstName, String lastName, String email, Integer from, Integer size) {
        List<Account> accounts = searchEntities(firstName, lastName, email, from, size);
        return mapper.toDtoList(accounts);
    }

    @Override
    public List<Account> searchEntities(String firstName, String lastName, String email, Integer from, Integer size) {
        Pageable pageable = new OffsetBasedPage(from, size);
        return repository.search(
                firstName,
                lastName,
                email,
                pageable
        );
    }

    @Override
    public AccountResponseDto update(Integer id, AccountRequestDto accountRequestDto) {
        Account account = mapper.toEntity(accountRequestDto);
        Account updatedAccount = updateEntity(id, account);
        return mapper.toDto(updatedAccount);
    }

    @Override
    public Account updateEntity(Integer id, Account account) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("acc-04", "id", String.valueOf(id)));

        String password = encodePassword(account.getEmail(), account.getPassword());
        Account accountToSave = account.toBuilder()
                .id(id)
                .password(password)
                .build();
        try {
            return repository.save(accountToSave);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("acc-06", "account", e.getLocalizedMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            Account account = readEntity(id);
            repository.delete(account);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("acc-05", "id", String.valueOf(id));
        }
    }

    private String encodePassword(String email, String password) {
        String textToEncode = email + ":" + password;
        return encoder.encode(textToEncode);
    }

}
