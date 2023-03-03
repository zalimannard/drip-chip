package ru.zalimannard.dripchip.account;

import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public AccountDto read(int id) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(id);
        accountDto.setFirstName("firstName");
        accountDto.setLastName("lastName");
        accountDto.setEmail("email@mail.ru");
        return accountDto;
    }

}
