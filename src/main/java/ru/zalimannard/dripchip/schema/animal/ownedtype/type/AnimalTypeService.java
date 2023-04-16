package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

import java.util.List;
import java.util.Set;

@Validated
public interface AnimalTypeService {

    AnimalTypeResponseDto create(@Valid AnimalTypeRequestDto animalTypeRequestDto);

    AnimalType createEntity(@Valid AnimalType animalType);


    AnimalTypeResponseDto read(@Positive long id);

    AnimalType readEntity(@Positive long id);

    List<AnimalType> readAllEntitiesById(Set<@Positive Long> ids);


    AnimalTypeResponseDto update(@Positive long id, @Valid AnimalTypeRequestDto animalTypeRequestDto);

    AnimalType updateEntity(@Positive long id, @Valid AnimalType animalType);


    void delete(@Positive long id);

}
