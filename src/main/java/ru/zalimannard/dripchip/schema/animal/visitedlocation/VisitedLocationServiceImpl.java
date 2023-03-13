package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.account.AccountRepository;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.animal.AnimalMapper;
import ru.zalimannard.dripchip.schema.animal.AnimalRepository;
import ru.zalimannard.dripchip.schema.animal.AnimalService;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.update.VisitedLocationUpdateDto;
import ru.zalimannard.dripchip.schema.location.Location;
import ru.zalimannard.dripchip.schema.location.LocationMapper;
import ru.zalimannard.dripchip.schema.location.LocationRepository;
import ru.zalimannard.dripchip.schema.location.LocationService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class VisitedLocationServiceImpl implements VisitedLocationService {

    private final AnimalRepository animalRepository;
    private final AccountRepository accountRepository;
    private final LocationRepository locationRepository;
    private final VisitedLocationRepository visitedLocationRepository;
    private final AnimalService animalService;
    private final LocationService locationService;
    private final VisitedLocationMapper visitedLocationMapper = Mappers.getMapper(VisitedLocationMapper.class);
    private final AnimalMapper animalMapper = Mappers.getMapper(AnimalMapper.class);
    private final LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

    @Override
    public VisitedLocationDto create(@Positive long animalId, @Positive long locationId) {
        Animal animal = animalMapper.toEntity(animalService.read(animalId), accountRepository, locationRepository);
        Location location = locationMapper.toEntity(locationService.read(locationId));

        if (animal.getLifeStatus() == AnimalLifeStatus.DEAD) {
            throw new BadRequestException("Dead animal can't visit new location");
        }
        List<VisitedLocation> allAnimalVisitedLocation =
                visitedLocationRepository.findAllByAnimalIdOrderByDateTimeOfVisitLocationPoint(animalId);
        if (allAnimalVisitedLocation.size() > 0) {
            if (location.equals(allAnimalVisitedLocation.get(allAnimalVisitedLocation.size() - 1).getLocation())) {
                throw new BadRequestException("The animal has just been in this location");
            }
        } else {
            if (location.equals(animal.getChippingLocation())) {
                throw new BadRequestException("The first visited location can't coincide with the chipping location");
            }
        }

        VisitedLocation visitedLocationRequest = new VisitedLocation();
        visitedLocationRequest.setDateTimeOfVisitLocationPoint(Timestamp.from(Instant.now()));
        visitedLocationRequest.setAnimal(animal);
        visitedLocationRequest.setLocation(location);

        VisitedLocation visitedLocationResponse = visitedLocationRepository.save(visitedLocationRequest);
        return visitedLocationMapper.toDto(visitedLocationResponse);
    }

    @Override
    public List<VisitedLocationDto> readAll(@Positive long animalId) {
        List<VisitedLocation> visitedLocations = visitedLocationRepository.findAllByAnimalId(animalId);
        return visitedLocationMapper.toDtoList(visitedLocations);
    }

    @Override
    public VisitedLocationDto update(@Positive long animalId, @Valid VisitedLocationUpdateDto visitedLocationUpdateDto) {
        long visitedLocationPointId = visitedLocationUpdateDto.getVisitedLocationPointId();
        long newLocationId = visitedLocationUpdateDto.getVisitedLocationPointId();

        Animal animal = animalMapper.toEntity(animalService.read(animalId), accountRepository, locationRepository);
        Location newLocation = locationMapper.toEntity(locationService.read(newLocationId));

        // TODO: Change to read/id when it is written
        VisitedLocation visitedLocation = visitedLocationRepository.findById(visitedLocationPointId)
                .orElseThrow(() -> new NotFoundException("Visited location", "id", String.valueOf(newLocationId)));
        // TODO: Change to search when it is written
        List<VisitedLocation> visitedLocations = visitedLocationRepository
                .findAllByAnimalIdOrderByDateTimeOfVisitLocationPoint(animalId);
        int visitedLocationPointArrayIndex = visitedLocations.indexOf(visitedLocation);

        if ((visitedLocationPointArrayIndex == 0) && (newLocation.equals(animal.getChippingLocation()))) {
            throw new BadRequestException("The first visited location can't coincide with the chipping location");
        }

        try {
            if (newLocation.equals(visitedLocations.get(visitedLocationPointArrayIndex - 1).getLocation())) {
                throw new BadRequestException("The updated element matches the one in front of it");
            }
        } catch (IndexOutOfBoundsException e) {
            // So the element is the first, everything is fine. Single element in front of it is checked
        }

        try {
            if (newLocation.equals(visitedLocations.get(visitedLocationPointArrayIndex - 1).getLocation())) {
                throw new BadRequestException("The updated element coincides with the following");
            }
        } catch (IndexOutOfBoundsException e) {
            // So the element is the last, everything is fine. There is nothing after it
        }

        visitedLocation.setLocation(newLocation);
        VisitedLocation visitedLocationResponse = visitedLocationRepository.save(visitedLocation);
        return visitedLocationMapper.toDto(visitedLocationResponse);
    }

    @Override
    public void delete(@Positive long animalId, @Positive long visitedLocationId) {
        if (animalRepository.existsById(animalId)) {
            if (visitedLocationRepository.existsById(visitedLocationId)) {
                visitedLocationRepository.deleteById(visitedLocationId);
            } else {
                throw new NotFoundException("Visited location", "id", String.valueOf(visitedLocationId));
            }
        } else {
            throw new NotFoundException("Animal", "id", String.valueOf(animalId));
        }
    }

}
