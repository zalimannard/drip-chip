package ru.zalimannard.dripchip.schema.animal.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.endpoint.animals}${application.endpoint.types}")
@Validated
@RequiredArgsConstructor
public class AnimalTypeController {

    private final AnimalTypeService animalTypeService;

    @GetMapping("{id}")
    public AnimalTypeDto get(@PathVariable long id) {
        return animalTypeService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalTypeDto post(@RequestBody AnimalTypeDto animalTypeDto) {
        return animalTypeService.create(animalTypeDto);
    }

    @PutMapping("{id}")
    public AnimalTypeDto put(@PathVariable long id,
                             @RequestBody AnimalTypeDto animalTypeDto) {
        return animalTypeService.update(id, animalTypeDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        animalTypeService.delete(id);
    }

}