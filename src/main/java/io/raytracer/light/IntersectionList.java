package io.raytracer.light;

import java.util.Optional;

public interface IntersectionList {
    int count();

    Intersection get(int i);

    Optional<Intersection> hit();
}
