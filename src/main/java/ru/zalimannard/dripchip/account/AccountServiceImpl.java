package ru.zalimannard.dripchip.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.ForbiddenException;
import ru.zalimannard.dripchip.exception.NotFoundException;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    private final PasswordEncoder encoder;

    @Override
    public AccountDto create(@Valid AccountDto accountDto) {
        Account accountRequest = accountMapper.toEntity(accountDto);
        accountRequest.setPassword(encoder.encode((accountRequest.getEmail() + ":" + accountRequest.getPassword())));
        try {
            Account accountResponse = accountRepository.save(accountRequest);
            return accountMapper.toDto(accountResponse);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public AccountDto read(int id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account", "id", String.valueOf(id)));
        return accountMapper.toDto(account);
    }

    @Override
    public List<AccountDto> search(AccountDto filter, int from, int size) {
        Account exampleAccount = accountMapper.toEntity(filter);

        List<Account> accountList = accountRepository.findAllByFirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCaseAndEmailLikeIgnoreCaseOrderById(
                "%" + (exampleAccount.getFirstName() == null ? "" : exampleAccount.getFirstName()) + "%",
                "%" + (exampleAccount.getLastName() == null ? "" : exampleAccount.getLastName()) + "%",
                "%" + (exampleAccount.getEmail() == null ? "" : exampleAccount.getEmail()) + "%");
        List<Account> responseAccountList = accountList
                .stream().skip(from)
                .limit(size).toList();

        return accountMapper.toDtoList(responseAccountList);
    }

    @Override
    public AccountDto update(int id, @Valid AccountDto accountDto) {
        if (accountRepository.existsById(id)) {
            Account accountRequest = accountMapper.toEntity(accountDto);
            accountRequest.setId(id);
            accountRequest.setPassword(encoder.encode((accountRequest.getEmail() + ":" + accountRequest.getPassword())));

            Account accountResponse = accountRepository.save(accountRequest);
            return accountMapper.toDto(accountResponse);
        } else {
            throw new ForbiddenException();
        }
    }

    @Override
    public void delete(int id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
        } else {
            throw new ForbiddenException();
        }
    }

}
