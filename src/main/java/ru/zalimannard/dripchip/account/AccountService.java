package ru.zalimannard.dripchip.account;

import jakarta.validation.Valid;

import java.util.List;

public interface AccountService {

    AccountDto create(@Valid AccountDto accountDto);

    AccountDto read(int id);

    List<AccountDto> search(AccountDto filter, int from, int size);

    AccountDto update(int id, @Valid AccountDto accountDto);

    void delete(int id);

}
