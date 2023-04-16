package ru.zalimannard.dripchip.integration.location;

import com.github.javafaker.Faker;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

public class LocationFactory {

    public static LocationRequestDto createLocationRequest() {
        return LocationRequestDto.builder()
                .longitude(Double.parseDouble(Faker.instance().address().longitude().replace(",", ".")))
                .latitude(Double.parseDouble(Faker.instance().address().latitude().replace(",", ".")))
                .build();
    }

    public static LocationResponseDto createLocationResponse(Long id, LocationRequestDto locationRequestDto) {
        return LocationResponseDto.builder()
                .id(id)
                .longitude(locationRequestDto.getLongitude())
                .latitude(locationRequestDto.getLatitude())
                .build();
    }

}