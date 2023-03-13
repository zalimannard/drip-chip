package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitedLocationRepository extends JpaRepository<VisitedLocation, Long> {

    List<VisitedLocation> findAllByAnimalIdOrderByDateTimeOfVisitLocationPoint(long animalId);

}