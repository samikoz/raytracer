package io.raytracer.mechanics;

import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;

public interface ILightSource {
    IColour illuminate(RayHit hitpoint);

    IPoint getPosition();
}
