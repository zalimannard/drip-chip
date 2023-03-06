package ru.zalimannard.dripchip.animal.visitedlocation;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.endpoint.animals}")
@Validated
@RequiredArgsConstructor
public class VisitedLocationController {

    private final VisitedLocationService visitedLocationService;

    // TODO: Change "locations" to path variable
    @PostMapping("{animalId}/locations/{locationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public VisitedLocationDto post(@PathVariable @Min(1) long animalId,
                                   @PathVariable @Min(1) long locationId) {
        return visitedLocationService.create(animalId, locationId);
    }

}
