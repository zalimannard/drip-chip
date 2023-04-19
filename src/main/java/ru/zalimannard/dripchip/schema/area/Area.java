package ru.zalimannard.dripchip.schema.area;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.zalimannard.dripchip.schema.area.point.Point;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "areas")
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "area")
    @Getter(AccessLevel.NONE)
    private List<Point> points;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Area area = (Area) o;
        return getId() != null && Objects.equals(getId(), area.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}