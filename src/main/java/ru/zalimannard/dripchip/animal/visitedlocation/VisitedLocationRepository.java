package ru.zalimannard.dripchip.animal.visitedlocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitedLocationRepository extends JpaRepository<VisitedLocation, Integer> {

}