package ru.zalimannard.dripchip.animal.type;

import jakarta.validation.Valid;

public interface AnimalTypeService {

    AnimalTypeDto create(@Valid AnimalTypeDto animalTypeDto);

    AnimalTypeDto read(long id);

    AnimalTypeDto update(long id, @Valid AnimalTypeDto animalTypeDto);

    void delete(long id);

}
