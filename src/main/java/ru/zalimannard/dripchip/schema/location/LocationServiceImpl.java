package ru.zalimannard.dripchip.schema.location;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    @Override
    public LocationDto create(LocationDto locationDto) {
        Location locationRequest = locationMapper.toEntity(locationDto);

        Location locationResponse = createEntity(locationRequest);

        return locationMapper.toDto(locationResponse);
    }

    @Override
    public Location createEntity(Location location) {
        return saveToDatabase(location);
    }

    @Override
    public LocationDto read(long id) {
        Location locationResponse = readEntity(id);

        return locationMapper.toDto(locationResponse);
    }

    @Override
    public Location readEntity(long id) {
        return locationRepository.findById(id).orElseThrow();
    }

    @Override
    public LocationDto update(long id, LocationDto locationDto) {
        Location locationRequest = locationMapper.toEntity(locationDto);

        Location locationResponse = updateEntity(id, locationRequest);

        return locationMapper.toDto(locationResponse);
    }

    @Override
    public Location updateEntity(long id, Location location) {
        if (locationRepository.existsById(id)) {
            location.setId(id);
            return saveToDatabase(location);
        } else {
            throw new NotFoundException("", "", "");
        }
    }

    @Override
    public void delete(long id) {
        try {
            Location location = readEntity(id);
            locationRepository.delete(location);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("", "", "");
        }
    }

    private Location saveToDatabase(Location location) {
        try {
            return locationRepository.save(location);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("", "", "");
        }
    }

}
