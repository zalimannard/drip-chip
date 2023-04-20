package ru.zalimannard.dripchip.schema.location;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

@RestController
@RequestMapping("${application.endpoint.locations}")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("{id}")
    public LocationResponseDto get(@PathVariable long id) {
        return locationService.read(id);
    }

    @GetMapping
    public Long get(@RequestParam Double longitude,
                    @RequestParam Double latitude) {
        return locationService.special1(longitude, latitude);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponseDto post(@RequestBody LocationRequestDto locationRequestDto) {
        return locationService.create(locationRequestDto);
    }

    @PutMapping("{id}")
    public LocationResponseDto put(@PathVariable long id,
                                   @RequestBody LocationRequestDto locationRequestDto) {
        return locationService.update(id, locationRequestDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        locationService.delete(id);
    }

}
