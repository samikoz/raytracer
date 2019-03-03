package io.raytracer.mathsy;

public interface TransformableVector extends Vector, Transformable {
    TransformableVector translate(Vector direction);
}
