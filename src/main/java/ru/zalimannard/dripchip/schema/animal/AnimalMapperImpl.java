package ru.zalimannard.dripchip.schema.animal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalType;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocation;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AnimalMapperImpl implements AnimalMapper {

    @Override
    public Animal toEntity(AnimalPostRequestDto dto,
                           List<AnimalType> animalTypes,
                           Account chipper,
                           Location chippingLocation) {
        return Animal.builder()
                .animalTypes(animalTypes)
                .weight(dto.getWeight())
                .length(dto.getLength())
                .height(dto.getHeight())
                .gender(animalGenderFromString(dto.getGender()))
                .lifeStatus(AnimalLifeStatus.ALIVE)
                .chipper(chipper.toBuilder().build())
                .chippingLocation(chippingLocation.toBuilder().build())
                .build();
    }

    @Override
    public Animal toEntity(AnimalPutRequestDto dto,
                           Account chipper,
                           Location chippingLocation) {
        return Animal.builder()
                .weight(dto.getWeight())
                .length(dto.getLength())
                .height(dto.getHeight())
                .gender(animalGenderFromString(dto.getGender()))
                .lifeStatus(animalLifeStatusFromString(dto.getLifeStatus()))
                .chipper(chipper.toBuilder().build())
                .chippingLocation(chippingLocation.toBuilder().build())
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

    private AnimalGender animalGenderFromString(String gender) {
        try {
            return AnimalGender.valueOf(gender);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("anm-01", "gender", gender);
        } catch (NullPointerException e) {
            throw new BadRequestException("anm-04", "gender", gender);
        }
    }

    private AnimalLifeStatus animalLifeStatusFromString(String lifeStatus) {
        try {
            return AnimalLifeStatus.valueOf(lifeStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("anm-02", "lifeStatus", lifeStatus);
        } catch (NullPointerException e) {
            throw new BadRequestException("anm-03", "lifeStatus", lifeStatus);
        }
    }

}
