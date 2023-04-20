package ru.zalimannard.dripchip.schema.area;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.area.dto.AreaRequestDto;
import ru.zalimannard.dripchip.schema.area.dto.AreaResponseDto;

@RestController
@RequestMapping("${application.endpoint.areas}")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    @GetMapping("{id}")
    public AreaResponseDto get(@PathVariable Long id) {
        return areaService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AreaResponseDto post(@RequestBody AreaRequestDto areaRequestDto) {
        return areaService.create(areaRequestDto);
    }

    @PutMapping("{id}")
    public AreaResponseDto put(@PathVariable Long id,
                               @RequestBody AreaRequestDto accountDto) {
        return areaService.update(id, accountDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        areaService.delete(id);
    }

}