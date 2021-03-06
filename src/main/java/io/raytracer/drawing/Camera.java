package io.raytracer.drawing;

import io.raytracer.geometry.ThreeTransformation;

public interface Camera {
    ThreeTransformation getTransformation();
}
