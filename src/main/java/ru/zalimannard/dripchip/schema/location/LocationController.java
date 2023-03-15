package ru.zalimannard.dripchip.schema.location;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.endpoint.locations}")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("{id}")
    public LocationDto get(@PathVariable long id) {
        return locationService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto post(@RequestBody LocationDto locationDto) {
        return locationService.create(locationDto);
    }

    @PutMapping("{id}")
    public LocationDto put(@PathVariable long id,
                           @RequestBody LocationDto locationDto) {
        return locationService.update(id, locationDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        locationService.delete(id);
    }

}
