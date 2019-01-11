package io.raytracer.structures;

public interface Point extends Tuple {
    Point add(Vector displacement);
}