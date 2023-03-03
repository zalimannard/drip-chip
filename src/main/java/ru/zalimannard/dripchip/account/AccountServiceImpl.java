package ru.zalimannard.dripchip.account;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

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

}
