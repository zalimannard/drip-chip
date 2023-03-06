package ru.zalimannard.dripchip.animal;

import jakarta.validation.Valid;

import java.util.List;

public interface AnimalService {

    AnimalDto create(@Valid AnimalDto animalDto);

    AnimalDto read(long id);

    List<AnimalDto> search(AnimalDto filter, int from, int size);

}
