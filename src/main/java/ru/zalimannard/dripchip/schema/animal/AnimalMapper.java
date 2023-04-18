package ru.zalimannard.dripchip.schema.animal;

import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalType;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.List;

public interface AnimalMapper {

    Animal toEntity(AnimalPostRequestDto dto,
                    List<AnimalType> animalTypes,
                    Account chipper,
                    Location chippingLocation);

    Animal toEntity(AnimalPutRequestDto dto,
                    Account chipper,
                    Location chippingLocation);

    AnimalResponseDto toDto(Animal entity);

    List<AnimalResponseDto> toDtoList(List<Animal> animals);

}
