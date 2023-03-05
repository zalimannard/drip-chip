package ru.zalimannard.dripchip.animal.type;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.endpoint.animals}")
@Validated
@RequiredArgsConstructor
public class AnimalTypeController {

    private final AnimalTypeService animalTypeService;

    @GetMapping("${application.endpoint.types}/{id}")
    public AnimalTypeDto get(@PathVariable @Min(1) long id) {
        return animalTypeService.read(id);
    }

    @PostMapping("${application.endpoint.types}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalTypeDto post(@RequestBody AnimalTypeDto animalTypeDto) {
        return animalTypeService.create(animalTypeDto);
    }

    @DeleteMapping("${application.endpoint.types}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @Min(1) long id) {
        animalTypeService.delete(id);
    }

}