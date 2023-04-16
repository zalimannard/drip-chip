package ru.zalimannard.dripchip.schema.location;

import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

public interface LocationMapper {

    Location toEntity(LocationRequestDto dto);

    LocationResponseDto toDto(Location entity);

}
