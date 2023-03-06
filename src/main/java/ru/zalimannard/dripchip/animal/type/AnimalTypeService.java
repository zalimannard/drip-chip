package ru.zalimannard.dripchip.animal.type;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.Set;

public interface AnimalTypeService {

    AnimalTypeDto create(@Valid AnimalTypeDto animalTypeDto);

    AnimalTypeDto read(@Min(1) long id);

    List<AnimalType> getAllById(Set<Long> ids);

    AnimalTypeDto update(long id, @Valid AnimalTypeDto animalTypeDto);

    void delete(long id);

}
