package ru.zalimannard.dripchip.schema.area.point;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.zalimannard.dripchip.schema.area.Area;

@Entity
@Table(name = "points")
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "longitude")
    @EqualsAndHashCode.Include
    private Double longitude;

    @Column(name = "latitude")
    @EqualsAndHashCode.Include
    private Double latitude;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    @Column(name = "number_in_area")
    private Long numberInArea;

}
