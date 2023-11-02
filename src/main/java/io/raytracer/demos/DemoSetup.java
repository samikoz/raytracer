package io.raytracer.demos;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.World;
import io.raytracer.tools.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.IOException;
import java.nio.file.Paths;


@Builder(toBuilder = true)
@AllArgsConstructor
public class DemoSetup {
    private final int rayCount;
    private final int xSize;
    private final int ySize;
    private final double viewAngle;
    private final IPoint eyePosition;
    private final IVector lookDirection;
    private final IVector upDirection;
    private final String filename;
    private final String bufferDir;
    private final int bufferFileCount;

    public IPicture picture;

    public void render(World world) throws IOException {
        Camera camera = new MultipleRayCamera(this.rayCount, this.xSize, this.ySize, this.viewAngle, this.eyePosition, this.lookDirection, this.upDirection);
        IPicture picture;
        if (this.bufferDir != null && bufferFileCount != 0) {
            picture = new BufferedPPMPicture(xSize, ySize, Paths.get(this.bufferDir), xSize * ySize / this.bufferFileCount);
        }
        else {
            picture = new PPMPicture(xSize, ySize);
        }
        world.render(picture, camera);
        this.picture = picture;
    }

    public void export() throws IOException {
        this.picture.export(Paths.get(this.filename));
    }
}
