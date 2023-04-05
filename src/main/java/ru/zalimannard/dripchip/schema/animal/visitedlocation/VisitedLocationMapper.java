package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.animal.AnimalService;
import ru.zalimannard.dripchip.schema.location.Location;
import ru.zalimannard.dripchip.schema.location.LocationService;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class VisitedLocationMapper {

    @Autowired
    private AnimalService animalService;
    @Autowired
    private LocationService locationService;

    @Mapping(target = "animal", ignore = true)
    @Mapping(target = "location", ignore = true)
    public abstract VisitedLocation toEntity(VisitedLocationDto dto);

    @Mapping(target = "animalId", source = "entity.animal.id")
    @Mapping(target = "locationId", source = "entity.location.id")
    public abstract VisitedLocationDto toDto(VisitedLocation entity);

    public abstract List<VisitedLocationDto> toDtoList(List<VisitedLocation> entity);

    @AfterMapping
    protected void toEntity(@MappingTarget VisitedLocation entity, VisitedLocationDto dto) {
        if (dto.getAnimalId() != null) {
            Animal animal = animalService.readEntity(dto.getAnimalId());
            entity.setAnimal(animal);
        }

        if (dto.getLocationId() != null) {
            Location location = locationService.readEntity(dto.getLocationId());
            entity.setLocation(location);
        }
    }

}
