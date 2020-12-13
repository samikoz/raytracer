package io.raytracer.light;

public class IntersectionListImpl implements IntersectionList {
    private final Intersection[] intersections;

    IntersectionListImpl(Intersection... intersections) {
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
}
