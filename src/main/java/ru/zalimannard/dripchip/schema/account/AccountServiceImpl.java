package ru.zalimannard.dripchip.schema.account;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.ForbiddenException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.page.OffsetBasedPage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder encoder;

    @Override
    public AccountDto create(AccountDto accountDto) {
        Account accountRequest = accountMapper.toEntity(accountDto);
        accountRequest.setPassword(encoder.encode((accountRequest.getEmail() + ":" + accountRequest.getPassword())));

        Account accountResponse = saveToDatabase(accountRequest);
        return accountMapper.toDto(accountResponse);
    }

    @Override
    public AccountDto read(int id) {
        checkExist(id);
        Account account = accountRepository.findById(id).get();
        return accountMapper.toDto(account);
    }

    @Override
    public List<AccountDto> search(AccountDto filterDto, int from, int size) {
        Account filter = accountMapper.toEntity(filterDto);
        Pageable pageable = new OffsetBasedPage(from, size);

        List<Account> accountList = accountRepository.search(filter.getLastName(), filter.getFirstName(),
                filter.getEmail(), pageable);

        return accountMapper.toDtoList(accountList);
    }

    @Override
    public AccountDto update(int id, AccountDto accountDto) {
        try {
            checkExist(id);
        } catch (NotFoundException e) {
            throw new ForbiddenException();
        }

        Account accountRequest = accountMapper.toEntity(accountDto);
        accountRequest.setId(id);
        accountRequest.setPassword(encoder.encode((accountRequest.getEmail() + ":" + accountRequest.getPassword())));

        Account accountResponse = saveToDatabase(accountRequest);
        return accountMapper.toDto(accountResponse);
    }

    @Override
    public void delete(int id) {
        try {
            checkExist(id);
        } catch (NotFoundException e) {
            throw new ForbiddenException();
        }

        try {
            accountRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("It is impossible to delete Account with id=" + id);
        }
    }

    private void checkExist(int id) {
        if (!accountRepository.existsById(id)) {
            throw new NotFoundException("Account", String.valueOf(id));
        }
    }

    private Account saveToDatabase(Account account) {
        try {
            return accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Account");
        }
    }

}
