package ru.zalimannard.dripchip.schema.area;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.area.dto.AreaRequestDto;
import ru.zalimannard.dripchip.schema.area.dto.AreaResponseDto;
import ru.zalimannard.dripchip.schema.area.point.PointMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AreaMapperImpl implements AreaMapper {

    private final PointMapper pointMapper;

    @Override
    public Area toEntity(AreaRequestDto dto) {
        return Area.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    public AreaResponseDto toDto(Area entity) {
        return AreaResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .points(pointMapper.toDtoList(entity.getPoints())).build();
    }

    @Override
    public List<AreaResponseDto> toDtoList(List<Area> entity) {
        return entity.stream().map(this::toDto).toList();
    }

}
