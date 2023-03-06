package ru.zalimannard.dripchip.animal.visitedlocation;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.animal.Animal;
import ru.zalimannard.dripchip.animal.AnimalRepository;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.location.Location;
import ru.zalimannard.dripchip.location.LocationRepository;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@Validated
@RequiredArgsConstructor
public class VisitedLocationServiceImpl implements VisitedLocationService {

    private final VisitedLocationRepository visitedLocationRepository;
    private final AnimalRepository animalRepository;
    private final LocationRepository locationRepository;
    private final VisitedLocationMapper visitedLocationMapper = Mappers.getMapper(VisitedLocationMapper.class);

    @Override
    public VisitedLocationDto create(long animalId, long locationId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new NotFoundException("Animal", "id", String.valueOf(animalId)));
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location", "id", String.valueOf(locationId)));

        VisitedLocation visitedLocationRequest = new VisitedLocation();
        visitedLocationRequest.setDateTimeOfVisitLocationPoint(Timestamp.from(Instant.now()));
        visitedLocationRequest.setAnimal(animal);
        visitedLocationRequest.setLocation(location);

        VisitedLocation visitedLocationResponse = visitedLocationRepository.save(visitedLocationRequest);
        return visitedLocationMapper.toDto(visitedLocationResponse);
    }

}
