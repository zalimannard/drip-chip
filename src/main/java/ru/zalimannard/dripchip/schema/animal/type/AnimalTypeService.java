package ru.zalimannard.dripchip.schema.animal.type;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Set;

public interface AnimalTypeService {

    AnimalTypeDto create(@Valid AnimalTypeDto animalTypeDto);

    AnimalTypeDto read(@Positive long id);

    List<AnimalType> getAllById(Set<@Positive Long> ids);

    AnimalTypeDto update(@Positive long id, @Valid AnimalTypeDto animalTypeDto);

    void delete(@Positive long id);

}
