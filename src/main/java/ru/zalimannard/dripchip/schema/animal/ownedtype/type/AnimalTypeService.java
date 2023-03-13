package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Validated
public interface AnimalTypeService {

    AnimalTypeDto create(@Valid AnimalTypeDto animalTypeDto);

    AnimalTypeDto read(@Positive long id);

    List<AnimalTypeDto> getAllById(Set<@Positive Long> ids);

    AnimalTypeDto update(@Positive long id, @Valid AnimalTypeDto animalTypeDto);

    void delete(@Positive long id);

}
