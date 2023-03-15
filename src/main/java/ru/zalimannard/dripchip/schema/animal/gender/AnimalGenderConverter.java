package ru.zalimannard.dripchip.schema.animal.gender;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class AnimalGenderConverter implements AttributeConverter<AnimalGender, String> {

    @Override
    public String convertToDatabaseColumn(AnimalGender attribute) {
        return attribute.getCode();
    }

    @Override
    public AnimalGender convertToEntityAttribute(String dbData) {
        return Stream.of(AnimalGender.values())
                .filter(c -> c.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
