package ru.zalimannard.dripchip.animal;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}