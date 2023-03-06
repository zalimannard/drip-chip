package ru.zalimannard.dripchip.animal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public interface AnimalService {

    AnimalDto create(@Valid AnimalDto animalDto);

    AnimalDto read(@Positive long id);

    List<AnimalDto> search(AnimalDto filter, @PositiveOrZero int from, @Positive int size);

    void delete(@Positive long id);

}
