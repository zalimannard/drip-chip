package ru.zalimannard.dripchip.schema.area;

import ru.zalimannard.dripchip.schema.area.dto.AreaRequestDto;
import ru.zalimannard.dripchip.schema.area.dto.AreaResponseDto;

import java.util.List;

public interface AreaMapper {

    Area toEntity(AreaRequestDto dto);

    AreaResponseDto toDto(Area entity);

    List<AreaResponseDto> toDtoList(List<Area> entity);

}