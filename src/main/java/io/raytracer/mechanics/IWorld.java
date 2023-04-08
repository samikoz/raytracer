package io.raytracer.mechanics;

import io.raytracer.tools.ICamera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.PPMPicture;
import io.raytracer.shapes.Shape;

public interface IWorld {
    IWorld put(ILightSource source);
    IWorld put(Shape object);

    IColour illuminate(IRay ray);

    default IPicture render(ICamera camera) {
        IPicture picture = new PPMPicture(camera.getPictureWidthPixels(), camera.getPictureHeightPixels());

        for (int y = 0; y < camera.getPictureHeightPixels() - 1; y++) {
            for (int x = 0; x < camera.getPictureWidthPixels() - 1; x++) {
                IRay ray = camera.getRayThroughPixel(x, y);
                IColour colour = this.illuminate(ray);
                picture.write(x, y, colour);
            }
        }

        return picture;
    }
}
