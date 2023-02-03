package io.raytracer.worldly;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class Intersections implements IIntersections {
    private final Intersection[] intersections;

    public Intersections(Intersection... intersections) {
        this.intersections = intersections;
    }

    @Override
    public int count() {
        return intersections.length;
    }

    @Override
    public Intersection get(int i) {
        try {
            return intersections[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + i);
        }
    }

    Intersections combine(Intersections others) {
        Intersection[] allIntersections = ArrayUtils.addAll(this.intersections, others.intersections);
        Arrays.sort(allIntersections, Comparator.comparingDouble(i -> i.rayParameter));
        return new Intersections(allIntersections);
    }

    @Override
    public Optional<Intersection> getHit() {
        return Arrays.stream(intersections).filter(i -> i.rayParameter > 0).min(Comparator.comparingDouble(i -> i.rayParameter));
    }
}
