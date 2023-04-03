package ru.zalimannard.dripchip.schema.area;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

public interface AreaService {

    AreaDto create(@Valid AreaDto areaDto);

    AreaDto read(@Positive long id);

    AreaDto update(@Positive long id, @Valid AreaDto accountDto);

    void delete(@Positive long id);

}
