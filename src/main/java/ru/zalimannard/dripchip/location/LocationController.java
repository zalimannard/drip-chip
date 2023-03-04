package ru.zalimannard.dripchip.location;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${application.endpoint.locations}")
@Validated
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("{id}")
    public LocationDto get(@PathVariable @Min(1) long id) {
        return locationService.read(id);
    }

}
