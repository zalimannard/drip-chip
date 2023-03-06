package ru.zalimannard.dripchip.animal;

import lombok.RequiredArgsConstructor;
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
    public AnimalDto get(@PathVariable long id) {
        return animalService.read(id);
    }

    @GetMapping("${application.endpoint.search}")
    public List<AnimalDto> search(AnimalDto filter,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return animalService.search(filter, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalDto post(@RequestBody AnimalDto animalDto) {
        return animalService.create(animalDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        animalService.delete(id);
    }

}