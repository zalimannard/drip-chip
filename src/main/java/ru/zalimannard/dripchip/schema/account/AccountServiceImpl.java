package ru.zalimannard.dripchip.schema.account;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.page.OffsetBasedPage;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;

    @Override
    public AccountResponseDto create(AccountRequestDto accountRequestDto) {
        Account accountRequest = accountMapper.toEntity(accountRequestDto);

        Account accountResponse = createEntity(accountRequest);

        return accountMapper.toDto(accountResponse);
    }

    @Override
    public Account createEntity(Account account) {
        return saveToDatabase(account);
    }

    @Override
    public AccountResponseDto read(int id) {
        Account accountResponse = readEntity(id);

        return accountMapper.toDto(accountResponse);
    }

    @Override
    public Account readEntity(int id) {
        return accountRepository.findById(id).
                orElseThrow(NotFoundException::new);
    }

    @Override
    public Account readEntityByEmail(String email) {
        return accountRepository.findByEmail(email);
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

        return accountRepository.search(
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
        if (accountRepository.existsById(id)) {
            account.setId(id);
            return saveToDatabase(account);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(int id) {
        try {
            Account account = readEntity(id);
            accountRepository.delete(account);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException();
        }
    }

    private Account saveToDatabase(Account account) {
        try {
            String textToEncode = account.getEmail() + ":" + account.getPassword();
            String password = encoder.encode(textToEncode);
            account.setPassword(password);
            return accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException();
        }
    }

}
