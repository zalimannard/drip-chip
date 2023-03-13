package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import org.mapstruct.*;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.animal.AnimalRepository;
import ru.zalimannard.dripchip.schema.location.Location;
import ru.zalimannard.dripchip.schema.location.LocationRepository;

import java.util.List;

@Mapper
public interface VisitedLocationMapper {

    @Mapping(target = "animal", ignore = true)
    @Mapping(target = "location", ignore = true)
    VisitedLocation toEntity(VisitedLocationDto dto);

    @Mapping(target = "animalId", ignore = true)
    @Mapping(target = "locationId", ignore = true)
    VisitedLocationDto toDto(VisitedLocation entity);

    List<VisitedLocation> toEntityList(List<VisitedLocationDto> dto);

    List<VisitedLocationDto> toDtoList(List<VisitedLocation> entity);

    @AfterMapping
    default void toEntity(@MappingTarget VisitedLocation entity, VisitedLocationDto dto,
                          @Context AnimalRepository animalRepository,
                          @Context LocationRepository locationRepository) {
        Animal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new NotFoundException("Animal", "id", String.valueOf(dto.getAnimalId())));
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new NotFoundException("Location", "id", String.valueOf(dto.getLocationId())));

        entity.setAnimal(animal);
        entity.setLocation(location);
    }

    @AfterMapping
    default void toDto(@MappingTarget VisitedLocationDto dto, VisitedLocation entity) {
        dto.setLocationId(entity.getLocation().getId());
    }

}
