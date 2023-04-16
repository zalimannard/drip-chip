package ru.zalimannard.dripchip.schema.location;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;

    @Override
    public LocationResponseDto create(LocationRequestDto locationRequestDto) {
        Location locationRequest = locationMapper.toEntity(locationRequestDto);

        Location locationResponse = createEntity(locationRequest);

        return locationMapper.toDto(locationResponse);
    }

    @Override
    public Location createEntity(Location location) {
        return saveToDatabase(location);
    }

    @Override
    public LocationResponseDto read(long id) {
        Location locationResponse = readEntity(id);

        return locationMapper.toDto(locationResponse);
    }

    @Override
    public Location readEntity(long id) {
        return locationRepository.findById(id).orElseThrow();
    }

    @Override
    public LocationResponseDto update(long id, LocationRequestDto locationRequestDto) {
        Location locationRequest = locationMapper.toEntity(locationRequestDto);

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
