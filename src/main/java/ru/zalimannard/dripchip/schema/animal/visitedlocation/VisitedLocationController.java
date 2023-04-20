package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationRequestUpdateDto;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationResponseDto;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("${application.endpoint.animals}/{animalId}")
@RequiredArgsConstructor
public class VisitedLocationController {

    private final VisitedLocationService visitedLocationService;

    @GetMapping("${application.endpoint.locations}")
    public List<VisitedLocationResponseDto> getAll(@PathVariable Long animalId,
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
    public VisitedLocationResponseDto post(@PathVariable Long animalId,
                                           @PathVariable Long locationId) {
        return visitedLocationService.create(animalId, locationId);
    }

    @PutMapping("${application.endpoint.locations}")
    public VisitedLocationResponseDto put(@PathVariable Long animalId,
                                          @RequestBody VisitedLocationRequestUpdateDto visitedLocationRequestUpdateDto) {
        return visitedLocationService.update(animalId, visitedLocationRequestUpdateDto);
    }

    @DeleteMapping("${application.endpoint.locations}/{visitedLocationId}")
    public void delete(@PathVariable Long animalId,
                       @PathVariable Long visitedLocationId) {
        visitedLocationService.delete(animalId, visitedLocationId);
    }

}
