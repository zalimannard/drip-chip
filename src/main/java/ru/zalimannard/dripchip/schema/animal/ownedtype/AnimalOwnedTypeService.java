package ru.zalimannard.dripchip.schema.animal.ownedtype;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalOwnedTypeUpdateDto;

@Validated
public interface AnimalOwnedTypeService {

    AnimalResponseDto create(@Positive @NotNull Long animalId, @Positive @NotNull Long typeId);

    Animal createEntity(@Positive @NotNull Long animalId, @Positive @NotNull Long typeId);


    AnimalResponseDto update(@Positive @NotNull Long animalId, @Valid AnimalOwnedTypeUpdateDto animalOwnedTypeUpdateDto);

    Animal updateEntity(@Positive @NotNull Long animalId, @Positive @NotNull Long oldTypeId, @Positive @NotNull Long newTypeId);


    void delete(@Positive @NotNull Long animalId, @Positive @NotNull Long typeId);

}
