package ru.zalimannard.dripchip.schema.animal.ownedtype;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.animal.AnimalDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.update.AnimalOwnedTypeUpdateDto;

@RestController
@RequestMapping("${application.endpoint.animals}/{animalId}")
@Validated
@RequiredArgsConstructor
public class AnimalOwnedTypeController {

    private final AnimalOwnedTypeService animalOwnedTypeService;

    @PostMapping("${application.endpoint.types}/{typeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalDto post(@PathVariable long animalId,
                          @PathVariable long typeId) {
        return animalOwnedTypeService.create(animalId, typeId);
    }

    @PutMapping("${application.endpoint.types}")
    public AnimalDto put(@PathVariable long animalId,
                         @RequestBody AnimalOwnedTypeUpdateDto animalOwnedTypeUpdateDto) {
        return animalOwnedTypeService.update(animalId, animalOwnedTypeUpdateDto);
    }

    @DeleteMapping("${application.endpoint.types}/{typeId}")
    public void delete(@PathVariable long animalId,
                       @PathVariable long typeId) {
        animalOwnedTypeService.delete(animalId, typeId);
    }

}
