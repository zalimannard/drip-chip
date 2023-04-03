package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zalimannard.dripchip.schema.animal.Animal;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitedLocationRepository extends JpaRepository<VisitedLocation, Long> {

    List<VisitedLocation> findAllByAnimalIdOrderByDateTimeOfVisitLocationPoint(long animalId);

    @Query("""
            SELECT
                v
            FROM
                VisitedLocation v
            WHERE
                ((cast(:start AS DATE) IS NULL) OR v.dateTimeOfVisitLocationPoint >= :start)
            AND
                ((cast(:end AS DATE) IS NULL) OR v.dateTimeOfVisitLocationPoint <= :end)
            AND
                (:animal IS NULL OR v.animal = :animal)
            ORDER BY
                v.dateTimeOfVisitLocationPoint
            """)
    List<VisitedLocation> search(@Param("start") Date start,
                                 @Param("end") Date end,
                                 @Param("animal") Animal animal,
                                 Pageable pageable);


}