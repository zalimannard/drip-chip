package ru.zalimannard.dripchip.animal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.sql.Timestamp;
import java.util.List;

public interface AnimalService {

    AnimalDto create(@Valid AnimalDto animalDto);

    AnimalDto read(@Positive long id);

    List<AnimalDto> search(AnimalDto filter, Timestamp start, Timestamp end, @PositiveOrZero int from,
                           @Positive int size);

    AnimalDto update(@Positive long id, @Valid AnimalDto animalDto);

    void delete(@Positive long id);

}
