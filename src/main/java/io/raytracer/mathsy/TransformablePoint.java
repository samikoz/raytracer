package io.raytracer.mathsy;

public interface TransformablePoint extends Point, Transformable {
    TransformablePoint translate(Vector direction);
}
