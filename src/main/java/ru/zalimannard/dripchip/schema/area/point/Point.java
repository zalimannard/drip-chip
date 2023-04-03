package ru.zalimannard.dripchip.schema.area.point;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.zalimannard.dripchip.schema.area.Area;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "latitude")
    @EqualsAndHashCode.Include
    private Double latitude;

    @Column(name = "longitude")
    @EqualsAndHashCode.Include
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    @Column(name = "number_in_area")
    private Long numberInArea;

}
