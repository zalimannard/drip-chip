package ru.zalimannard.dripchip.schema.area.point;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import ru.zalimannard.dripchip.exception.BadRequestException;

@Value
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class Segment {

    Point a;
    Point b;

    public double distance() {
        double xDiff = a.getLongitude() - b.getLongitude();
        double yDiff = a.getLatitude() - b.getLatitude();

        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    public boolean hasIntersection(Segment other) {
        if (isVertical() && other.isVertical()) {
            return false;
        } else if (isVertical() || other.isVertical()) {
            Segment verticalSegment = isVertical() ?
                    toBuilder().build() :
                    other.toBuilder().build();
            Segment inclinedSegment = isVertical() ?
                    other.toBuilder().build() :
                    toBuilder().build();
            double slopeCoefficient = inclinedSegment.slopeCoefficient();
            double absoluteTerm = inclinedSegment.absoluteTerm();

            double xIntersection = verticalSegment.a.getLongitude();
            double yIntersection = slopeCoefficient * verticalSegment.a.getLongitude() + absoluteTerm;
            // The vertical segment does not intersect the point
            if ((Double.compare(verticalSegment.a.getLatitude(), yIntersection) < 0) && (Double.compare(verticalSegment.b.getLatitude(), yIntersection) < 0) ||
                    (Double.compare(verticalSegment.a.getLatitude(), yIntersection) > 0) && (Double.compare(verticalSegment.b.getLatitude(), yIntersection) > 0)) {
                return false;
            }
            // The inclined segment does not intersect the point
            return ((Double.compare(inclinedSegment.a.getLongitude(), xIntersection) > 0) || (Double.compare(inclinedSegment.b.getLongitude(), xIntersection) > 0)) &&
                    ((Double.compare(inclinedSegment.a.getLongitude(), xIntersection) < 0) || (Double.compare(inclinedSegment.b.getLongitude(), xIntersection) < 0));
        } else {
            double thisLeft = Math.min(a.getLongitude(), b.getLongitude());
            double thisRight = Math.max(a.getLongitude(), b.getLongitude());
            double otherLeft = Math.min(other.a.getLongitude(), other.b.getLongitude());
            double otherRight = Math.max(other.a.getLongitude(), other.b.getLongitude());

            if (Double.compare(slopeCoefficient(), other.slopeCoefficient()) == 0) {
                return false;
            } else {
                double xIntersection = (other.absoluteTerm() - absoluteTerm()) /
                        (slopeCoefficient() - other.slopeCoefficient());

                return ((Double.compare(thisLeft, xIntersection) < 0) &&
                        (Double.compare(xIntersection, thisRight) < 0) &&
                        (Double.compare(otherLeft, xIntersection) < 0) &&
                        (Double.compare(xIntersection, otherRight) < 0));
            }
        }
    }

    public double slopeCoefficient() {
        if (isHorizontal()) {
            return 0.0;
        } else if (isVertical()) {
            throw new BadRequestException();
        } else {
            return (b.getLatitude() - a.getLatitude()) /
                    (b.getLongitude() - a.getLongitude());
        }
    }

    public double absoluteTerm() {
        if (isHorizontal()) {
            return a.getLatitude();
        } else if (isVertical()) {
            throw new BadRequestException();
        } else {
            return -((a.getLongitude() * b.getLatitude()) / (b.getLongitude() - a.getLongitude())) +
                    ((a.getLatitude() * b.getLongitude()) / (b.getLongitude() - a.getLongitude()));
        }
    }

    private boolean isHorizontal() {
        return Double.compare(a.getLatitude(), b.getLatitude()) == 0;
    }

    private boolean isVertical() {
        return Double.compare(a.getLongitude(), b.getLongitude()) == 0;
    }

}
