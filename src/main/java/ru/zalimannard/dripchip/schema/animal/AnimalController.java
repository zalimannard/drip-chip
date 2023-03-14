package ru.zalimannard.dripchip.schema.animal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
                                  @RequestParam(required = false) @DateTimeFormat(iso =
                                          DateTimeFormat.ISO.DATE_TIME) Date startDateTime,
                                  @RequestParam(required = false) @DateTimeFormat(iso =
                                          DateTimeFormat.ISO.DATE_TIME) Date endDateTime,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return animalService.search(filter, startDateTime, endDateTime, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalDto post(@RequestBody AnimalDto animalDto) {
        return animalService.create(animalDto);
    }

    @PutMapping("{id}")
    public AnimalDto put(@PathVariable long id,
                         @RequestBody AnimalDto accountDto) {
        return animalService.update(id, accountDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        animalService.delete(id);
    }

}