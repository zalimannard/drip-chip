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

    public double calcArea() {
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

    public boolean haveIdenticallyPoints(Area other) {
        if (getPoints().size() != other.getPoints().size()) {
            return false;
        }

        List<Point> thisPoints = getPoints();
        List<Point> otherPoints = other.getPoints();
        // Проверяем для каждого сдвига
        for (int i = 0; i < thisPoints.size(); ++i) {
            // Каждую точку
            for (int j = 0; j < thisPoints.size(); ++j) {
                Point currentThisPoint = thisPoints.get(j);
                Point currentOtherPoint = otherPoints.get((j + i) % thisPoints.size());

                if ((Double.compare(currentThisPoint.getLongitude(), currentOtherPoint.getLongitude()) == 0)
                    && (Double.compare(currentThisPoint.getLatitude(), currentOtherPoint.getLatitude()) == 0)) {
                    if (j == thisPoints.size() - 1) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        // И для развёрнутого
        for (int i = 0; i < thisPoints.size(); ++i) {
            for (int j = 0; j < thisPoints.size(); ++j) {
                Point currentThisPoint = thisPoints.get(thisPoints.size() - j - 1);
                Point currentOtherPoint = otherPoints.get((j + i) % thisPoints.size());

                if ((Double.compare(currentThisPoint.getLongitude(), currentOtherPoint.getLongitude()) == 0)
                        && (Double.compare(currentThisPoint.getLatitude(), currentOtherPoint.getLatitude()) == 0)) {
                    if (j == thisPoints.size() - 1) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }

        return false;
    }

    public boolean haveInside(Point point) {
        return true;
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