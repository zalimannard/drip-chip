package ru.zalimannard.dripchip.schema.location;

import ch.hsr.geohash.GeoHash;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationMapper mapper;
    private final LocationRepository repository;

    @Override
    public LocationResponseDto create(LocationRequestDto locationRequestDto) {
        Location locationRequest = mapper.toEntity(locationRequestDto);
        Location locationResponse = createEntity(locationRequest);
        return mapper.toDto(locationResponse);
    }

    @Override
    public Location createEntity(Location location) {
        try {
            return repository.save(location);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("loc-01", "location", e.getLocalizedMessage());
        }
    }

    @Override
    public LocationResponseDto read(long id) {
        Location locationResponse = readEntity(id);
        return mapper.toDto(locationResponse);
    }

    @Override
    public Location readEntity(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("loc-02", "id", String.valueOf(id)));
    }

    @Override
    public LocationResponseDto update(long id, LocationRequestDto locationRequestDto) {
        Location locationRequest = mapper.toEntity(locationRequestDto);
        Location locationResponse = updateEntity(id, locationRequest);
        return mapper.toDto(locationResponse);
    }

    @Override
    public Location updateEntity(long id, Location location) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("loc-03", "id", String.valueOf(id)));
        Location locationToSave = location.toBuilder()
                .id(id)
                .build();

        try {
            return repository.save(locationToSave);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("loc-05", "id", String.valueOf(id));
        }
    }

    @Override
    public void delete(long id) {
        try {
            Location location = readEntity(id);
            repository.delete(location);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("loc-04", "id", String.valueOf(id));
        }
    }

    @Override
    public Long special1(Double longitude, Double latitude) {
        List<Location> allLocation = repository.findAll();
        for (Location location : allLocation) {
            if ((location.getLongitude().equals(longitude)) && (location.getLatitude().equals(latitude))) {
                return location.getId();
            }
        }
        throw new NotFoundException("loc-06", "longitude-latitude", longitude + " " + latitude);
    }

    @Override
    public String special2(Double longitude, Double latitude) {
        return GeoHash.withCharacterPrecision(latitude, longitude, 12).toBase32();
    }

    @Override
    public String special3(Double longitude, Double latitude) {
        String coordinatesAsGeohash = special2(longitude, latitude);
        return Base64.getEncoder().encodeToString(coordinatesAsGeohash.getBytes());
    }

}
