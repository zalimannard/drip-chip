package ru.zalimannard.dripchip.animal;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${application.endpoint.animals}")
@Validated
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("{id}")
    public AnimalDto get(@PathVariable @Min(1) long id) {
        return animalService.read(id);
    }

    @GetMapping("${application.endpoint.search}")
    public List<AnimalDto> search(@QuerydslPredicate AnimalDto filter,
                                  @RequestParam(defaultValue = "0") @Min(0) int from,
                                  @RequestParam(defaultValue = "10") @Min(1) int size) {
        return animalService.search(filter, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalDto post(@RequestBody AnimalDto animalDto) {
        return animalService.create(animalDto);
    }

}