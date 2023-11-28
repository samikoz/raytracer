package io.raytracer.shapes;

import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;

public enum Axis {
    X(new Vector(1, 0, 0)),
    Y(new Vector(0, 1, 0)),
    Z(new Vector(0, 0, 1));

    private final IVector direction;

    private Axis(IVector direction) {
        this.direction = direction;
    }
}

