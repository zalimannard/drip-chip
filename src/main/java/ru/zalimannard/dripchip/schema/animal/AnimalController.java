package ru.zalimannard.dripchip.schema.animal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("${application.endpoint.animals}")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("{id}")
    public AnimalResponseDto get(@PathVariable long id) {
        return animalService.read(id);
    }

    @GetMapping("${application.endpoint.search}")
    public List<AnimalResponseDto> search(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDateTime,
                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDateTime,
                                          @RequestParam(required = false) Integer chipperId,
                                          @RequestParam(required = false) Integer chippingLocationId,
                                          @RequestParam(required = false) String lifeStatus,
                                          @RequestParam(required = false) String gender,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        return animalService.search(startDateTime, endDateTime, chipperId, chippingLocationId, lifeStatus, gender, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponseDto post(@RequestBody AnimalPostRequestDto animalPostRequestDto) {
        return animalService.create(animalPostRequestDto);
    }

    @PutMapping("{id}")
    public AnimalResponseDto put(@PathVariable long id,
                                 @RequestBody AnimalPutRequestDto animalPutRequestDto) {
        return animalService.update(id, animalPutRequestDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        animalService.delete(id);
    }

}