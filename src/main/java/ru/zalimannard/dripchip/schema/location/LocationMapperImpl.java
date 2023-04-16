package ru.zalimannard.dripchip.schema.location;

import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

@Component
public class LocationMapperImpl implements LocationMapper {

    @Override
    public Location toEntity(LocationRequestDto dto) {
        return Location.builder()
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .build();
    }

    @Override
    public LocationResponseDto toDto(Location entity) {
        return LocationResponseDto.builder()
                .id(entity.getId())
                .longitude(entity.getLongitude())
                .latitude(entity.getLatitude()).build();
    }

}
