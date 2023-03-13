package ru.zalimannard.dripchip.schema.animal;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountDto;
import ru.zalimannard.dripchip.schema.account.AccountMapper;
import ru.zalimannard.dripchip.schema.account.AccountService;
import ru.zalimannard.dripchip.schema.animal.type.AnimalType;
import ru.zalimannard.dripchip.schema.animal.type.AnimalTypeDto;
import ru.zalimannard.dripchip.schema.animal.type.AnimalTypeMapper;
import ru.zalimannard.dripchip.schema.animal.type.AnimalTypeService;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocation;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocationDto;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocationMapper;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocationService;
import ru.zalimannard.dripchip.schema.location.Location;
import ru.zalimannard.dripchip.schema.location.LocationDto;
import ru.zalimannard.dripchip.schema.location.LocationMapper;
import ru.zalimannard.dripchip.schema.location.LocationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class AnimalMapper {

    @Autowired
    private AnimalTypeService animalTypeService;
    @Autowired
    private AnimalTypeMapper animalTypeMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationMapper locationMapper;

    @Mapping(target = "animalTypes", ignore = true)
    @Mapping(target = "visitedLocations", ignore = true)
    @Mapping(target = "chipper", ignore = true)
    @Mapping(target = "chippingLocation", ignore = true)
//    @Mapping(target = "visitedLocations", ignore = true)
    public abstract Animal toEntity(AnimalDto dto);

    @Mapping(target = "animalTypeIds", ignore = true)
    @Mapping(target = "visitedLocations", ignore = true)
    @Mapping(target = "chipperId", source = "entity.chipper.id")
    @Mapping(target = "chippingLocationId", source = "entity.chippingLocation.id")
    public abstract AnimalDto toDto(Animal entity);

    public abstract List<Animal> toEntityList(List<AnimalDto> dto);

    public abstract List<AnimalDto> toDtoList(List<Animal> entity);

    @AfterMapping
    protected void toEntity(@MappingTarget Animal entity, AnimalDto dto) {
        if (dto.getAnimalTypeIds() != null) {
            List<AnimalTypeDto> animalTypesDto = animalTypeService.getAllById(dto.getAnimalTypeIds());
            List<AnimalType> animalTypes = animalTypeMapper.toEntityList(animalTypesDto);
            entity.setAnimalTypes(new HashSet<>(animalTypes));
        }

        if (dto.getChipperId() != null) {
            AccountDto chipperDto = accountService.read(dto.getChipperId());
            Account chipper = accountMapper.toEntity(chipperDto);
            entity.setChipper(chipper);
        }

        if (dto.getChippingLocationId() != null) {
            LocationDto locationDto = locationService.read(dto.getChippingLocationId());
            Location location = locationMapper.toEntity(locationDto);
            entity.setChippingLocation(location);
        }
    }

    @AfterMapping
    protected void toDto(@MappingTarget AnimalDto dto, Animal entity) {
        for (AnimalType animalType : entity.getAnimalTypes()) {
            dto.addAnimalTypeId(animalType.getId());
        }
        List<VisitedLocation> visitedLocations = entity.getVisitedLocations();
        if (visitedLocations == null) {
            visitedLocations = new ArrayList<>();
        }
        List<Long> visitedLocationsIds = new ArrayList<>();
        visitedLocations.forEach(visitedLocation -> visitedLocationsIds.add(visitedLocation.getId()));
        dto.setVisitedLocations(visitedLocationsIds);
    }

}