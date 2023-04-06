package ru.zalimannard.dripchip.schema.area;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.zalimannard.dripchip.schema.area.point.Point;

import java.util.ArrayList;
import java.util.Comparator;
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
    @Getter(AccessLevel.NONE)
    private List<Point> points = new ArrayList<>();

    public List<Point> getPoints() {
        points.sort(Comparator.comparingLong(Point::getNumberInArea));

        return points;
    }

    public double calcAreaValue() {
        double area = 0;
        List<Point> pointList = getPoints();

        for (int i = 0; i < pointList.size(); ++i) {
            area += pointList.get(i).getLongitude() *
                    pointList.get((i + 1) % pointList.size()).getLatitude();
        }

        return area;
    }

}