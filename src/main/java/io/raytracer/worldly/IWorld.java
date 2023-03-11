package io.raytracer.worldly;

import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.IColour;
import io.raytracer.drawing.IPicture;
import io.raytracer.drawing.PPMPicture;
import io.raytracer.worldly.drawables.Drawable;
import lombok.NonNull;

public interface IWorld {
    IWorld put(ILightSource source);
    IWorld put(Drawable object);

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
