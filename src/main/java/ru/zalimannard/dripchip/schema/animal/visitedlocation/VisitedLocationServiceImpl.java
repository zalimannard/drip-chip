package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.page.OffsetBasedPage;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.animal.AnimalDto;
import ru.zalimannard.dripchip.schema.animal.AnimalMapper;
import ru.zalimannard.dripchip.schema.animal.AnimalService;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.update.VisitedLocationUpdateDto;
import ru.zalimannard.dripchip.schema.location.Location;
import ru.zalimannard.dripchip.schema.location.LocationDto;
import ru.zalimannard.dripchip.schema.location.LocationMapper;
import ru.zalimannard.dripchip.schema.location.LocationService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitedLocationServiceImpl implements VisitedLocationService {

    private final VisitedLocationRepository visitedLocationRepository;
    private final VisitedLocationMapper visitedLocationMapper;
    private final AnimalService animalService;
    private final AnimalMapper animalMapper;
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @Override
    public VisitedLocationDto create(long animalId, long locationId) {
        AnimalDto animalDto = animalService.read(animalId);
        Animal animal = animalMapper.toEntity(animalDto);
        LocationDto locationDto = locationService.read(locationId);
        Location location = locationMapper.toEntity(locationDto);

        if (animal.getLifeStatus() == AnimalLifeStatus.DEAD) {
            throw new BadRequestException("Dead animal can't visit new location");
        }

        ArrayList<VisitedLocation> allAnimalVisitedLocations = new ArrayList<>(getAllVisitedLocation(animal));
        VisitedLocation newVisitedLocation = new VisitedLocation();
        newVisitedLocation.setLocation(location);
        allAnimalVisitedLocations.add(newVisitedLocation);

        checkOrderOfVisit(allAnimalVisitedLocations);

        VisitedLocation visitedLocationRequest = new VisitedLocation();
        visitedLocationRequest.setDateTimeOfVisitLocationPoint(Date.from(Instant.now()));
        visitedLocationRequest.setAnimal(animal);
        visitedLocationRequest.setLocation(location);

        VisitedLocation visitedLocationResponse = saveToDatabase(visitedLocationRequest);
        return visitedLocationMapper.toDto(visitedLocationResponse);
    }

    @Override
    public VisitedLocationDto read(long id) {
        VisitedLocation visitedLocation = visitedLocationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Visited location", String.valueOf(id)));
        return visitedLocationMapper.toDto(visitedLocation);
    }

    @Override
    public List<VisitedLocationDto> search(long animalId, Date startDateTime, Date endDateTime, int from, int size) {
        AnimalDto animalDto = animalService.read(animalId);
        Animal animal = animalMapper.toEntity(animalDto);
        Pageable pageable = new OffsetBasedPage(from, size);

        List<VisitedLocation> visitedLocations = visitedLocationRepository.search(startDateTime, endDateTime, animal,
                pageable);
        return visitedLocationMapper.toDtoList(visitedLocations);
    }

    @Override
    public VisitedLocationDto update(long animalId, VisitedLocationUpdateDto visitedLocationUpdateDto) {
        AnimalDto animalDto = animalService.read(animalId);
        Animal animal = animalMapper.toEntity(animalDto);

        long visitedLocationPointId = visitedLocationUpdateDto.getVisitedLocationPointId();
        VisitedLocationDto visitedLocationDto = read(visitedLocationPointId);
        VisitedLocation visitedLocation = visitedLocationMapper.toEntity(visitedLocationDto);
        checkHasAnimalVisitedLocation(animal, visitedLocation.getId());

        long locationPointId = visitedLocationUpdateDto.getLocationPointId();
        LocationDto locationDto = locationService.read(locationPointId);
        Location newLocation = locationMapper.toEntity(locationDto);

        ArrayList<VisitedLocation> allAnimalVisitedLocations = getAllVisitedLocation(animal);
        int indexOfVisitedLocationForUpdate = allAnimalVisitedLocations.indexOf(visitedLocation);
        allAnimalVisitedLocations.get(indexOfVisitedLocationForUpdate).setLocation(newLocation);

        checkOrderOfVisit(allAnimalVisitedLocations);

        if (visitedLocation.getLocation().equals(newLocation)) {
            throw new BadRequestException("Replacing the last point with the same point");
        }

        visitedLocation.setLocation(newLocation);
        VisitedLocation visitedLocationResponse = saveToDatabase(visitedLocation);
        return visitedLocationMapper.toDto(visitedLocationResponse);
    }

    @Override
    public void delete(long animalId, long visitedLocationId) {
        AnimalDto animalDto = animalService.read(animalId);
        Animal animal = animalMapper.toEntity(animalDto);
        VisitedLocationDto visitedLocationDto = read(visitedLocationId);
        VisitedLocation visitedLocation = visitedLocationMapper.toEntity(visitedLocationDto);

        checkHasAnimalVisitedLocation(animal, visitedLocationId);

        ArrayList<VisitedLocation> allAnimalVisitedLocations = getAllVisitedLocation(animal);
        int indexOfVisitedLocationToDelete = allAnimalVisitedLocations.indexOf(visitedLocation);
        allAnimalVisitedLocations.remove(indexOfVisitedLocationToDelete);

        // If the chip point coincides with the first, delete first
        if ((indexOfVisitedLocationToDelete == 1) && (allAnimalVisitedLocations.size() > 1)) {
            Location chippingLocation = animal.getChippingLocation();
            Location firstVisitedLocation = allAnimalVisitedLocations.get(1).getLocation();
            if (chippingLocation.equals(firstVisitedLocation)) {
                visitedLocationRepository.deleteById(allAnimalVisitedLocations.get(1).getId());
                allAnimalVisitedLocations.remove(1);
            }
        }
        checkOrderOfVisit(allAnimalVisitedLocations);

        visitedLocationRepository.deleteById(visitedLocationId);
    }

    private void checkOrderOfVisit(ArrayList<VisitedLocation> orderOfVisit) {
        for (int i = 0; i < orderOfVisit.size() - 1; ++i) {
            Location prevLocation = orderOfVisit.get(i).getLocation();
            Location nextLocation = orderOfVisit.get(i + 1).getLocation();
            if (prevLocation.equals(nextLocation)) {
                throw new BadRequestException("Two visited points in a row are the same");
            }
        }
    }

    private void checkHasAnimalVisitedLocation(Animal animal, long visitedLocationId) {
        List<VisitedLocation> visitedLocations = visitedLocationRepository.findAllByAnimalIdOrderByDateTimeOfVisitLocationPoint(animal.getId());
        if (visitedLocations == null) {
            visitedLocations = new ArrayList<>();
        }
        VisitedLocation visitedLocation = new VisitedLocation();
        visitedLocation.setId(visitedLocationId);
        if (!visitedLocations.contains(visitedLocation)) {
            throw new NotFoundException("Visited Location", visitedLocationId + " at animal with id=" + animal.getId());
        }
    }

    private ArrayList<VisitedLocation> getAllVisitedLocation(Animal animal) {
        VisitedLocation chippingVisitedLocation = new VisitedLocation();
        chippingVisitedLocation.setLocation(animal.getChippingLocation());

        ArrayList<VisitedLocation> visitedLocations = new ArrayList<>(List.of(chippingVisitedLocation));
        visitedLocations.addAll(visitedLocationRepository.findAllByAnimalIdOrderByDateTimeOfVisitLocationPoint(animal.getId()));
        return visitedLocations;
    }

    private VisitedLocation saveToDatabase(VisitedLocation visitedLocation) {
        try {
            return visitedLocationRepository.save(visitedLocation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Visited location");
        }
    }

}
