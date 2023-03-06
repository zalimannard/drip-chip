package ru.zalimannard.dripchip.animal;

import org.mapstruct.*;
import ru.zalimannard.dripchip.account.Account;
import ru.zalimannard.dripchip.account.AccountRepository;
import ru.zalimannard.dripchip.animal.type.AnimalType;
import ru.zalimannard.dripchip.animal.type.AnimalTypeRepository;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.location.Location;
import ru.zalimannard.dripchip.location.LocationRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface AnimalMapper {

    @Mapping(target = "animalTypes", ignore = true)
    @Mapping(target = "chipper", ignore = true)
    @Mapping(target = "chippingLocation", ignore = true)
    Animal toEntity(AnimalDto dto,
                    @Context AnimalTypeRepository animalTypeRepository,
                    @Context AccountRepository accountRepository,
                    @Context LocationRepository locationRepository);

    @Mapping(target = "animalTypeIds", ignore = true)
    @Mapping(target = "visitedLocations", ignore = true)
    @Mapping(target = "chipperId", ignore = true)
    @Mapping(target = "chippingLocationId", ignore = true)
    AnimalDto toDto(Animal entity);

    @AfterMapping
    default void toEntity(@MappingTarget Animal entity, AnimalDto dto,
                          @Context AnimalTypeRepository animalTypeRepository,
                          @Context AccountRepository accountRepository,
                          @Context LocationRepository locationRepository) {
        Set<AnimalType> animalTypes = new HashSet<>(animalTypeRepository.findAllById(dto.getAnimalTypeIds()));
        entity.setAnimalTypes(animalTypes);

        Optional<Account> chipper = accountRepository.findById(dto.getChipperId());
        if (chipper.isPresent()) {
            entity.setChipper(chipper.get());
        } else {
            throw new NotFoundException("Account", "id", String.valueOf(dto.getChipperId()));
        }

        Optional<Location> location = locationRepository.findById(dto.getChippingLocationId());
        if (location.isPresent()) {
            entity.setChippingLocation(location.get());
        } else {
            throw new NotFoundException("Location", "id", String.valueOf(dto.getChippingLocationId()));
        }
    }

    @AfterMapping
    default void toDto(@MappingTarget AnimalDto dto, Animal entity) {
        dto.setAnimalTypeIds(new HashSet<>());
        for (AnimalType animalType : entity.getAnimalTypes()) {
            dto.addAnimalTypeId(animalType.getId());
        }

        dto.setChipperId(entity.getChipper().getId());

        dto.setChippingLocationId(entity.getChippingLocation().getId());
    }

}
