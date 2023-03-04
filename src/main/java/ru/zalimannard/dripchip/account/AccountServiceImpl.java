package ru.zalimannard.dripchip.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Override
    public AccountDto create(@Valid AccountDto accountDto) {
        System.out.println(accountDto.toString());
        Account accountRequest = accountMapper.toEntity(accountDto);
        Account accountResponse = accountRepository.save(accountRequest);
        return accountMapper.toDto(accountResponse);
    }

    @Override
    public AccountDto read(int id) {
        if (id > 3) {
            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Account", "id", String.valueOf(id)));
            return accountMapper.toDto(account);
        } else {
            // TODO: Delete when registration is implemented
            AccountDto accountDto = new AccountDto();
            accountDto.setId(id);
            accountDto.setFirstName("firstName");
            accountDto.setLastName("lastName");
            accountDto.setEmail("email@mail.ru");
            return accountDto;
        }
    }

    @Override
    public List<AccountDto> search(AccountDto filter, int from, int size) {
        return new ArrayList<>();
    }

}
