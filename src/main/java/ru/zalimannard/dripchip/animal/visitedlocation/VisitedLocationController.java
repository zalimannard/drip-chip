package ru.zalimannard.dripchip.animal.visitedlocation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${application.endpoint.animals}/{animalId}")
@Validated
@RequiredArgsConstructor
public class VisitedLocationController {

    private final VisitedLocationService visitedLocationService;

    @GetMapping("${application.endpoint.locations}")
    public List<VisitedLocationDto> getAll(@PathVariable long animalId) {
        return visitedLocationService.readAll(animalId);
    }

    @PostMapping("${application.endpoint.locations}/{locationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public VisitedLocationDto post(@PathVariable long animalId,
                                   @PathVariable long locationId) {
        return visitedLocationService.create(animalId, locationId);
    }

}
