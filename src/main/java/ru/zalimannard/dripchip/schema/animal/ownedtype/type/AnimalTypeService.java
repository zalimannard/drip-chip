package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Validated
public interface AnimalTypeService {

    AnimalTypeDto create(@Valid AnimalTypeDto animalTypeDto);

    AnimalType createEntity(@Valid AnimalType animalType);


    AnimalTypeDto read(@Positive long id);

    AnimalType readEntity(@Positive long id);

    List<AnimalType> readAllEntitiesById(Set<@Positive Long> ids);


    AnimalTypeDto update(@Positive long id, @Valid AnimalTypeDto animalTypeDto);

    AnimalType updateEntity(@Positive long id, @Valid AnimalType animalType);


    void delete(@Positive long id);

}
