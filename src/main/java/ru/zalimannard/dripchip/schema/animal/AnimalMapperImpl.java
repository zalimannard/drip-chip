package ru.zalimannard.dripchip.schema.animal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.account.AccountService;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalType;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeService;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocation;
import ru.zalimannard.dripchip.schema.location.LocationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AnimalMapperImpl implements AnimalMapper {

    private final AnimalTypeService animalTypeService;
    private final AccountService accountService;
    private final LocationService locationService;

    @Override
    public Animal toEntity(AnimalPostRequestDto dto) {
        return Animal.builder()
                .animalTypes(new HashSet<>(animalTypeService.readAllEntitiesById(dto.getAnimalTypeIds())))
                .weight(dto.getWeight())
                .length(dto.getLength())
                .height(dto.getHeight())
                .gender(dto.getGender())
                .lifeStatus(AnimalLifeStatus.ALIVE)
                .chipper(accountService.readEntity(dto.getChipperId()))
                .chippingLocation(locationService.readEntity(dto.getChippingLocationId()))
                .build();
    }

    @Override
    public Animal toEntity(AnimalPutRequestDto dto) {
        return Animal.builder()
                .weight(dto.getWeight())
                .length(dto.getLength())
                .height(dto.getHeight())
                .gender(dto.getGender())
                .lifeStatus(AnimalLifeStatus.ALIVE)
                .chipper(accountService.readEntity(dto.getChipperId()))
                .chippingLocation(locationService.readEntity(dto.getChippingLocationId()))
                .build();
    }

    @Override
    public AnimalResponseDto toDto(Animal entity) {
        return AnimalResponseDto.builder()
                .id(entity.getId())
                .animalTypeIds(entity.getAnimalTypes().stream()
                        .map(AnimalType::getId)
                        .collect(Collectors.toSet()))
                .weight(entity.getWeight())
                .length(entity.getLength())
                .height(entity.getHeight())
                .gender(entity.getGender())
                .lifeStatus(entity.getLifeStatus())
                .chippingDateTime(entity.getChippingDateTime())
                .chipperId(entity.getChipper().getId())
                .chippingLocationId(entity.getChippingLocation().getId())
                .visitedLocations(entity.getVisitedLocations() == null
                        ? new ArrayList<>()
                        : entity.getVisitedLocations().stream().map(VisitedLocation::getId).collect(Collectors.toList()))
                .deathDateTime(entity.getDeathDateTime())
                .build();
    }

    @Override
    public List<AnimalResponseDto> toDtoList(List<Animal> animals) {
        return animals.stream().map(this::toDto).toList();
    }

}
