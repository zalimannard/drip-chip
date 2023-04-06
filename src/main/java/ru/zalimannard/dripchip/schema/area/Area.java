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
        double value1 = 0;
        double value2 = 0;
        List<Point> pointList = getPoints();

        for (int i = 0; i < pointList.size(); ++i) {
            value1 += pointList.get(i).getLongitude() *
                    pointList.get((i + 1) % pointList.size()).getLatitude();
            value2 += pointList.get(i).getLatitude() *
                    pointList.get((i + 1) % pointList.size()).getLongitude();
        }

        return value1 - value2;
    }

}