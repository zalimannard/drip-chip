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

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;

    @Override
    public AccountDto create(AccountDto accountDto) {
        Account accountRequest = accountMapper.toEntity(accountDto);

        Account accountResponse = createEntity(accountRequest);

        return accountMapper.toDto(accountResponse);
    }

    @Override
    public Account createEntity(Account account) {
        String password = encoder.encode((account.getEmail() + ":" + account.getPassword()));

        account.setPassword(password);

        return saveToDatabase(account);
    }

    @Override
    public AccountDto read(int id) {
        Account accountResponse = readEntity(id);

        return accountMapper.toDto(accountResponse);
    }

    @Override
    public Account readEntity(int id) {
        return accountRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Account readEntityByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public List<AccountDto> search(AccountDto filterDto, int from, int size) {
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
    public AccountDto update(int id, AccountDto accountDto) {
        Account accountRequest = accountMapper.toEntity(accountDto);

        Account accountResponse = updateEntity(id, accountRequest);

        return accountMapper.toDto(accountResponse);
    }

    @Override
    public Account updateEntity(int id, Account account) {
        if (accountRepository.existsById(id)) {
            account.setId(id);
            String password = encoder.encode((account.getEmail() + ":" + account.getPassword()));
            account.setPassword(password);
            return saveToDatabase(account);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(int id) {
        if (accountRepository.existsById(id)) {
            try {
                accountRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    private Account saveToDatabase(Account account) {
        try {
            return accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException();
        }
    }

}
