package ru.zalimannard.dripchip.animal;

import org.mapstruct.*;
import ru.zalimannard.dripchip.account.Account;
import ru.zalimannard.dripchip.account.AccountRepository;
import ru.zalimannard.dripchip.animal.type.AnimalType;
import ru.zalimannard.dripchip.animal.visitedlocation.VisitedLocation;
import ru.zalimannard.dripchip.animal.visitedlocation.VisitedLocationDto;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.location.Location;
import ru.zalimannard.dripchip.location.LocationRepository;

import java.util.HashSet;
import java.util.List;

@Mapper
public interface AnimalMapper {

    @Mapping(target = "animalTypes", ignore = true)
    @Mapping(target = "chipper", ignore = true)
    @Mapping(target = "chippingLocation", ignore = true)
    Animal toEntity(AnimalDto dto,
                    @Context AccountRepository accountRepository,
                    @Context LocationRepository locationRepository);

    @Mapping(target = "animalTypeIds", ignore = true)
    @Mapping(target = "visitedLocations", ignore = true)
    @Mapping(target = "chipperId", ignore = true)
    @Mapping(target = "chippingLocationId", ignore = true)
    AnimalDto toDto(Animal entity);

    List<Animal> toEntityList(List<AnimalDto> dto,
                              @Context AccountRepository accountRepository,
                              @Context LocationRepository locationRepository);

    List<AnimalDto> toDtoList(List<Animal> entity);

    @AfterMapping
    default void toEntity(@MappingTarget Animal entity, AnimalDto dto,
                          @Context AccountRepository accountRepository,
                          @Context LocationRepository locationRepository) {
        Account chipper = accountRepository.findById(dto.getChipperId())
                .orElseThrow(() -> new NotFoundException("Account", "id", String.valueOf(dto.getChipperId())));
        Location location = locationRepository.findById(dto.getChippingLocationId())
                .orElseThrow(() -> new NotFoundException("Location", "id", String.valueOf(dto.getChippingLocationId())));

        entity.setChipper(chipper);
        entity.setChippingLocation(location);
    }

    @AfterMapping
    default void toDto(@MappingTarget AnimalDto dto, Animal entity) {
        for (AnimalType animalType : entity.getAnimalTypes()) {
            dto.addAnimalTypeId(animalType.getId());
        }

        dto.setChipperId(entity.getChipper().getId());

        dto.setChippingLocationId(entity.getChippingLocation().getId());
    }

}
