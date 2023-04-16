package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

public interface AnimalTypeMapper {

    AnimalType toEntity(AnimalTypeRequestDto dto);

    AnimalTypeResponseDto toDto(AnimalType entity);

}
