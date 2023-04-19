package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationRequestDto;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationResponseDto;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.List;

@Component
public class VisitedLocationMapperImpl implements VisitedLocationMapper {

    @Override
    public VisitedLocation toEntity(VisitedLocationRequestDto dto,
                                    Animal animal,
                                    Location location) {
        return VisitedLocation.builder()
                .dateTimeOfVisitLocationPoint(dto.getDateTimeOfVisitLocationPoint())
                .animal(animal.toBuilder().build())
                .location(location.toBuilder().build())
                .build();
    }

    @Override
    public VisitedLocationResponseDto toDto(VisitedLocation entity) {
        return VisitedLocationResponseDto.builder()
                .id(entity.getId())
                .dateTimeOfVisitLocationPoint(entity.getDateTimeOfVisitLocationPoint())
                .locationId(entity.getLocation().getId())
                .build();
    }

    @Override
    public List<VisitedLocationResponseDto> toDtoList(List<VisitedLocation> entity) {
        return entity.stream().map(this::toDto).toList();
    }

}
