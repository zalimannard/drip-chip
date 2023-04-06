package ru.zalimannard.dripchip.schema.area;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.endpoint.areas}")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    @GetMapping("{id}")
    public AreaDto get(@PathVariable long id) {
        return areaService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AreaDto post(@RequestBody AreaDto areaDto) {
        return areaService.create(areaDto);
    }

    @PutMapping("{id}")
    public AreaDto put(@PathVariable long id,
                       @RequestBody AreaDto accountDto) {
        return areaService.update(id, accountDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        areaService.delete(id);
    }

}