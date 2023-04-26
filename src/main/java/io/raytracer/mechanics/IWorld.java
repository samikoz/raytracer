package io.raytracer.mechanics;

import io.raytracer.tools.ICamera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.PPMPicture;
import io.raytracer.shapes.Shape;

import java.util.Collection;

public interface IWorld {
    IWorld put(ILightSource source);
    IWorld put(Shape object);

    IColour illuminate(Collection<IRay> rays);

    default IPicture render(ICamera camera) {
        int totalRaysCount = camera.getPictureWidthPixels()*camera.getPictureHeightPixels();
        IPicture picture = new PPMPicture(camera.getPictureWidthPixels(), camera.getPictureHeightPixels());

        int rayCount = 1;
        for (int y = 0; y < camera.getPictureHeightPixels() - 1; y++) {
            for (int x = 0; x < camera.getPictureWidthPixels() - 1; x++) {
                System.out.printf("\rray %d out of %d", rayCount, totalRaysCount);
                Collection<IRay> rays = camera.getRaysThroughPixel(x, y);
                IColour colour = this.illuminate(rays);
                picture.write(x, y, colour);
                rayCount++;
            }
        }
        System.out.println();

        return picture;
    }
}
