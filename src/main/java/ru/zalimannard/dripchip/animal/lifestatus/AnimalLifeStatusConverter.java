package ru.zalimannard.dripchip.animal.lifestatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class AnimalLifeStatusConverter implements AttributeConverter<AnimalLifeStatus, String> {

    @Override
    public String convertToDatabaseColumn(AnimalLifeStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public AnimalLifeStatus convertToEntityAttribute(String dbData) {
        return Stream.of(AnimalLifeStatus.values())
                .filter(c -> c.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
