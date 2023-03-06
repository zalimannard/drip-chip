package ru.zalimannard.dripchip.animal.visitedlocation;

public interface VisitedLocationService {

    VisitedLocationDto create(long animalId, long locationId);

}
