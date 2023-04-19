package ru.zalimannard.dripchip.schema.account;

import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

import java.util.List;

@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toEntity(AccountRequestDto dto) {
        return Account.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(accountRoleFromString(dto.getRole()))
                .build();
    }

    @Override
    public Account toEntity(AuthenticationDto dto) {
        return Account.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    @Override
    public AccountResponseDto toDto(Account entity) {
        return AccountResponseDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }

    @Override
    public List<AccountResponseDto> toDtoList(List<Account> accounts) {
        return accounts.stream().map(this::toDto).toList();
    }

    private AccountRole accountRoleFromString(String role) {
        try {
            return AccountRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("rol-01", "role", role);
        } catch (NullPointerException e) {
            throw new BadRequestException("rol-02", "role", role);
        }
    }

}
