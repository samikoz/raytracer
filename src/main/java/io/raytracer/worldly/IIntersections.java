package io.raytracer.worldly;

import java.util.Optional;

public interface IIntersections {
    int count();
    Intersection get(int i);
    Optional<Hit> getHit();
}
