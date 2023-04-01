package ru.zalimannard.dripchip.schema.account.role;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class AccountRoleConverter implements AttributeConverter<AccountRole, String> {

    @Override
    public String convertToDatabaseColumn(AccountRole attribute) {
        return attribute.getCode();
    }

    @Override
    public AccountRole convertToEntityAttribute(String dbData) {
        return Stream.of(AccountRole.values())
                .filter(c -> c.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
