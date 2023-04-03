package ru.zalimannard.dripchip.schema.area;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.zalimannard.dripchip.schema.area.point.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "areas")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "area")
    private List<Point> points = new ArrayList<>();

}