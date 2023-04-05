package ru.zalimannard.dripchip.schema.animal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Date;
import java.util.List;

public interface AnimalService {

    AnimalDto create(@Valid AnimalDto animalDto);

    Animal createEntity(@Valid Animal animal);


    AnimalDto read(@Positive long id);

    Animal readEntity(@Positive long id);

    List<AnimalDto> search(AnimalDto filterDto, Date start, Date end, @PositiveOrZero int from, @Positive int size);

    List<Animal> searchEntities(Animal filter, Date start, Date end, @PositiveOrZero int from, @Positive int size);


    AnimalDto update(@Positive long id, @Valid AnimalDto animalDto);

    Animal updateEntity(@Positive long id, @Valid Animal animal);


    void delete(@Positive long id);

}
