package ru.zalimannard.dripchip.schema.animal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Date;
import java.util.List;

public interface AnimalService {

    AnimalDto create(@Valid AnimalDto animalDto);

    AnimalDto read(@Positive long id);

    List<AnimalDto> search(AnimalDto filter, Date start, Date end, @PositiveOrZero int from,
                           @Positive int size);

    AnimalDto update(@Positive long id, @Valid AnimalDto animalDto);

    void delete(@Positive long id);

}
