package ru.zalimannard.dripchip.location;

import jakarta.validation.Valid;

public interface LocationService {

    LocationDto create(@Valid LocationDto locationDto);

    LocationDto read(long id);

    LocationDto update(long id, @Valid LocationDto locationDto);

    void delete(long id);

}
