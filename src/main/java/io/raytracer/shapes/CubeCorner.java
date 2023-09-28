package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;

public enum CubeCorner {
    FIRST(new Point(1, 1, 1)),
    SECOND(new Point(-1, 1, 1)),
    THIRD(new Point(1, -1, 1)),
    FOURTH(new Point(-1, -1, -1)),
    FIFTH(new Point(1, -1, -1)),
    SIXTH(new Point(-1, 1, -1));

    private final IPoint coords;

    private CubeCorner(IPoint coords) {
        this.coords = coords;
    }

    public int x() {
        return (int) this.coords.get(0);
    }

    public int y() {
        return (int) this.coords.get(1);
    }

    public int z() {
        return (int) this.coords.get(2);
    }
}
