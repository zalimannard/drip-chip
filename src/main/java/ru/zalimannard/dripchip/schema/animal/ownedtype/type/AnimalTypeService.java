package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

import java.util.List;
import java.util.Set;

@Validated
public interface AnimalTypeService {

    AnimalTypeResponseDto create(@Valid AnimalTypeRequestDto animalTypeRequestDto);

    AnimalType createEntity(AnimalType animalType);


    AnimalTypeResponseDto read(@Positive @NotNull Long id);

    AnimalType readEntity(@Positive @NotNull Long id);

    List<AnimalType> readAllEntitiesById(Set<@Positive @NotNull Long> ids);


    AnimalTypeResponseDto update(@Positive @NotNull Long id, @Valid AnimalTypeRequestDto animalTypeRequestDto);

    AnimalType updateEntity(@Positive @NotNull Long id, AnimalType animalType);


    void delete(@Positive @NotNull Long id);

}
