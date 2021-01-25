package io.raytracer.light;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class IntersectionListImpl implements IntersectionList {
    private final Intersection[] intersections;

    public IntersectionListImpl(Intersection... intersections) {
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

    IntersectionListImpl combine(IntersectionListImpl others) {
        Intersection[] allIntersections = ArrayUtils.addAll(this.intersections, others.intersections);
        Arrays.sort(allIntersections, Comparator.comparingDouble(i -> i.time));
        return new IntersectionListImpl(allIntersections);
    }

    @Override
    public Optional<Intersection> hit() {
        return Arrays.stream(intersections).filter(i -> i.time > 0).min(Comparator.comparingDouble(i -> i.time));
    }
}
