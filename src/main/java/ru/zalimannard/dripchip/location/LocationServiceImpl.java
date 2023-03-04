package ru.zalimannard.dripchip.location;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

    @Override
    public LocationDto read(long id) {
        if (id > 3) {
            Location location = locationRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Location", "id", String.valueOf(id)));
            return locationMapper.toDto(location);
        } else {
            LocationDto locationDto = new LocationDto();
            locationDto.setId(id);
            locationDto.setLatitude(1.0);
            locationDto.setLongitude(1.0);
            return locationDto;
        }
    }

}
