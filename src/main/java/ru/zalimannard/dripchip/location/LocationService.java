package ru.zalimannard.dripchip.location;

import jakarta.validation.Valid;

public interface LocationService {

    LocationDto create(@Valid LocationDto locationDto);

    LocationDto read(long id);

    void delete(long id);

}
