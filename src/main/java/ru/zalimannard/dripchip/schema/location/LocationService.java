package ru.zalimannard.dripchip.schema.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
public interface LocationService {

    LocationDto create(@Valid LocationDto locationDto);

    Location createEntity(@Valid Location location);


    LocationDto read(@Positive long id);

    Location readEntity(@Positive long id);


    LocationDto update(@Positive long id, @Valid LocationDto locationDto);

    Location updateEntity(@Positive long id, @Valid Location location);


    void delete(@Positive long id);

}
