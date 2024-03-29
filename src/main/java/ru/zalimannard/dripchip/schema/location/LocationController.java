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
    public LocationResponseDto get(@PathVariable Long id) {
        return locationService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponseDto post(@RequestBody LocationRequestDto locationRequestDto) {
        return locationService.create(locationRequestDto);
    }

    @PutMapping("{id}")
    public LocationResponseDto put(@PathVariable Long id,
                                   @RequestBody LocationRequestDto locationRequestDto) {
        return locationService.update(id, locationRequestDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        locationService.delete(id);
    }

    @GetMapping
    public Long getSpecial1(@RequestParam Double longitude,
                            @RequestParam Double latitude) {
        return locationService.special1(longitude, latitude);
    }

    @GetMapping("${application.endpoint.geohash}")
    public String getSpecial2(@RequestParam Double longitude,
                              @RequestParam Double latitude) {
        return locationService.special2(longitude, latitude);
    }

    @GetMapping("${application.endpoint.geohash}v2")
    public String getSpecial3(@RequestParam Double longitude,
                              @RequestParam Double latitude) {
        return locationService.special3(longitude, latitude);
    }

}
