package ru.zalimannard.dripchip.animal.visitedlocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitedLocationRepository extends JpaRepository<VisitedLocation, Long> {

    List<VisitedLocation> findAllByAnimalId(long animalId);

    List<VisitedLocation> findAllByAnimalIdOrderByDateTimeOfVisitLocationPoint(long animalId);

}