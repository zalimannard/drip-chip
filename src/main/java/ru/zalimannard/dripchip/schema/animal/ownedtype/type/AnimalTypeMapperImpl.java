package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

@Component
public class AnimalTypeMapperImpl implements AnimalTypeMapper {

    @Override
    public AnimalType toEntity(AnimalTypeRequestDto dto) {
        return AnimalType.builder()
                .type(dto.getType())
                .build();
    }

    @Override
    public AnimalTypeResponseDto toDto(AnimalType entity) {
        return AnimalTypeResponseDto.builder()
                .id(entity.getId())
                .type(entity.getType())
                .build();
    }

}
