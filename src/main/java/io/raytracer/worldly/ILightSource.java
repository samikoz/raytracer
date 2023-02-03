package io.raytracer.worldly;

import io.raytracer.drawing.IColour;

public interface ILightSource {
    IColour illuminate(MaterialPoint illuminated);
}
