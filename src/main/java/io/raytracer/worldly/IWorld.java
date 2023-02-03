package io.raytracer.worldly;

import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.IPicture;

public interface IWorld {
    IWorld put(ILightSource source);
    IWorld put(IDrawable object);

    IPicture render(ICamera camera);
}
