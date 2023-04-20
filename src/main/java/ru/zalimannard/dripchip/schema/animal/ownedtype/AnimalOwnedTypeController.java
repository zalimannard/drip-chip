package ru.zalimannard.dripchip.schema.animal.ownedtype;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalOwnedTypeUpdateDto;

@RestController
@RequestMapping("${application.endpoint.animals}/{animalId}")
@RequiredArgsConstructor
public class AnimalOwnedTypeController {

    private final AnimalOwnedTypeService animalOwnedTypeService;

    @PostMapping("${application.endpoint.types}/{typeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponseDto post(@PathVariable Long animalId,
                                  @PathVariable Long typeId) {
        return animalOwnedTypeService.create(animalId, typeId);
    }

    @PutMapping("${application.endpoint.types}")
    public AnimalResponseDto put(@PathVariable Long animalId,
                                 @RequestBody AnimalOwnedTypeUpdateDto animalOwnedTypeUpdateDto) {
        return animalOwnedTypeService.update(animalId, animalOwnedTypeUpdateDto);
    }

    @DeleteMapping("${application.endpoint.types}/{typeId}")
    public void delete(@PathVariable Long animalId,
                       @PathVariable Long typeId) {
        animalOwnedTypeService.delete(animalId, typeId);
    }

}
