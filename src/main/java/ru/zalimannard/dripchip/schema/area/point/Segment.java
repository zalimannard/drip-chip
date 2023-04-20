package ru.zalimannard.dripchip.schema.area.point;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class Segment {

    Point a;
    Point b;

    private static double orientation(Point p, Point q, Point r) {
        return (q.getLatitude() - p.getLatitude()) * (r.getLongitude() - q.getLongitude())
                - (q.getLongitude() - p.getLongitude()) * (r.getLatitude() - q.getLatitude());
    }

    public double distance() {
        double xDiff = a.getLongitude() - b.getLongitude();
        double yDiff = a.getLatitude() - b.getLatitude();

        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    public boolean hasIntersection(Segment other) {
        double orientationP1 = orientation(a, b, other.a);
        double orientationP2 = orientation(a, b, other.b);
        double orientationQ1 = orientation(other.a, other.b, a);
        double orientationQ2 = orientation(other.a, other.b, b);

        return orientationP1 * orientationP2 < 0 && orientationQ1 * orientationQ2 < 0;
    }

}
