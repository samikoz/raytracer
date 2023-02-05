package io.raytracer.worldly;

import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.IPicture;
import io.raytracer.worldly.drawables.Drawable;

public interface IWorld {
    IWorld put(ILightSource source);
    IWorld put(Drawable object);

    IPicture render(ICamera camera);
}
