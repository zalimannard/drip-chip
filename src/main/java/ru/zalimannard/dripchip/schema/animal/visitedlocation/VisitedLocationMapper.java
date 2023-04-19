package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationRequestDto;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationResponseDto;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.List;

public interface VisitedLocationMapper {

    VisitedLocation toEntity(VisitedLocationRequestDto dto,
                             Animal animal,
                             Location location);

    VisitedLocationResponseDto toDto(VisitedLocation entity);

    List<VisitedLocationResponseDto> toDtoList(List<VisitedLocation> entity);

}
