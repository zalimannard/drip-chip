package ru.zalimannard.dripchip.schema.animal.ownedtype;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.animal.AnimalDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.update.AnimalOwnedTypeUpdateDto;

@Validated
public interface AnimalOwnedTypeService {

    AnimalDto create(@Positive long animalId, @Positive long typeId);

    AnimalDto update(@Positive long animalId, @Valid AnimalOwnedTypeUpdateDto animalOwnedTypeUpdateDto);

    void delete(@Positive long animalId, @Positive long typeId);

}
