package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

@RestController
@RequestMapping("${application.endpoint.animals}${application.endpoint.types}")
@RequiredArgsConstructor
public class AnimalTypeController {

    private final AnimalTypeService animalTypeService;

    @GetMapping("{id}")
    public AnimalTypeResponseDto get(@PathVariable @Positive @NotNull long id) {
        return animalTypeService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalTypeResponseDto post(@RequestBody @Valid AnimalTypeRequestDto animalTypeRequestDto) {
        return animalTypeService.create(animalTypeRequestDto);
    }

    @PutMapping("{id}")
    public AnimalTypeResponseDto put(@PathVariable @Positive @NotNull long id,
                                     @RequestBody @Valid AnimalTypeRequestDto animalTypeRequestDto) {
        return animalTypeService.update(id, animalTypeRequestDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @Positive @NotNull long id) {
        animalTypeService.delete(id);
    }

}