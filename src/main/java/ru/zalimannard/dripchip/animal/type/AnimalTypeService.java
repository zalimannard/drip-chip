package ru.zalimannard.dripchip.animal.type;

import jakarta.validation.Valid;

public interface AnimalTypeService {

    AnimalTypeDto create(@Valid AnimalTypeDto animalTypeDto);

    AnimalTypeDto read(long id);

    void delete(long id);

}
