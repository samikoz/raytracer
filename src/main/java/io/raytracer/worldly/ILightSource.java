package io.raytracer.worldly;

import io.raytracer.drawing.IColour;
import io.raytracer.geometry.IPoint;

public interface ILightSource {
    IColour illuminate(MaterialPoint illuminated);

    IPoint getPosition();
}
