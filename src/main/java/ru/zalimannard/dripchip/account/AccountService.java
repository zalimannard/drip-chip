package ru.zalimannard.dripchip.account;

import java.util.List;

public interface AccountService {

    AccountDto read(int id);

    List<AccountDto> search(AccountDto filter, int from, int size);

}
