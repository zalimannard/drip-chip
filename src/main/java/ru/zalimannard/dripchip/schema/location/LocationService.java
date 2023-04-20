package ru.zalimannard.dripchip.schema.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

@Validated
public interface LocationService {

    LocationResponseDto create(@Valid LocationRequestDto locationRequestDto);

    Location createEntity(Location location);


    LocationResponseDto read(@Positive @NotNull Long id);

    Location readEntity(@Positive @NotNull Long id);


    LocationResponseDto update(@Positive @NotNull Long id, @Valid LocationRequestDto locationRequestDto);

    Location updateEntity(@Positive @NotNull Long id, Location location);


    void delete(@Positive @NotNull Long id);


    Long special1(@NotNull @Min(-180) @Max(180) Double longitude, @NotNull @Min(-90) @Max(90) Double latitude);

    String special2(@NotNull @Min(-180) @Max(180) Double longitude, @NotNull @Min(-90) @Max(90) Double latitude);

    String special3(@NotNull @Min(-180) @Max(180) Double longitude, @NotNull @Min(-90) @Max(90) Double latitude);

}
