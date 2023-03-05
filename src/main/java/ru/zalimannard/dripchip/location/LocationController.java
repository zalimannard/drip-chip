package ru.zalimannard.dripchip.location;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto post(@RequestBody LocationDto locationDto) {
        return locationService.create(locationDto);
    }

    @PutMapping("{id}")
    public LocationDto put(@PathVariable @Min(1) long id,
                           @RequestBody LocationDto locationDto) {
        return locationService.update(id, locationDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @Min(1) long id) {
        locationService.delete(id);
    }

}
