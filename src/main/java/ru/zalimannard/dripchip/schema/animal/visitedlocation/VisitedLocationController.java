package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationResponseDto;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationUpdateDto;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("${application.endpoint.animals}/{animalId}")
@RequiredArgsConstructor
public class VisitedLocationController {

    private final VisitedLocationService visitedLocationService;

    @GetMapping("${application.endpoint.locations}")
    public List<VisitedLocationResponseDto> getAll(@PathVariable long animalId,
                                                   @RequestParam(required = false) @DateTimeFormat(iso =
                                                           DateTimeFormat.ISO.DATE_TIME) Date startDateTime,
                                                   @RequestParam(required = false) @DateTimeFormat(iso =
                                                           DateTimeFormat.ISO.DATE_TIME) Date endDateTime,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return visitedLocationService.search(animalId, startDateTime, endDateTime, from, size);
    }

    @PostMapping("${application.endpoint.locations}/{locationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public VisitedLocationResponseDto post(@PathVariable long animalId,
                                           @PathVariable long locationId) {
        return visitedLocationService.create(animalId, locationId);
    }

    @PutMapping("${application.endpoint.locations}")
    public VisitedLocationResponseDto put(@PathVariable long animalId,
                                          @RequestBody VisitedLocationUpdateDto visitedLocationUpdateDto) {
        return visitedLocationService.update(animalId, visitedLocationUpdateDto);
    }

    @DeleteMapping("${application.endpoint.locations}/{visitedLocationId}")
    public void delete(@PathVariable long animalId,
                       @PathVariable long visitedLocationId) {
        visitedLocationService.delete(animalId, visitedLocationId);
    }

}
