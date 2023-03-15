package ru.zalimannard.dripchip.schema.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
public interface LocationService {

    LocationDto create(@Valid LocationDto locationDto);

    LocationDto read(@Positive long id);

    LocationDto update(@Positive long id, @Valid LocationDto locationDto);

    void delete(@Positive long id);

}
