package ru.zalimannard.dripchip.schema.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

@Validated
public interface LocationService {

    LocationResponseDto create(@Valid LocationRequestDto locationRequestDto);

    Location createEntity(@Valid Location location);


    LocationResponseDto read(@Positive long id);

    Location readEntity(@Positive long id);


    LocationResponseDto update(@Positive long id, @Valid LocationRequestDto locationRequestDto);

    Location updateEntity(@Positive long id, @Valid Location location);


    void delete(@Positive long id);

}
