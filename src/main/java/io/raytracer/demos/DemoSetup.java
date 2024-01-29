package io.raytracer.demos;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.tools.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.IOException;
import java.nio.file.Paths;


@Builder(toBuilder = true)
@AllArgsConstructor
public class DemoSetup {
    public final int rayCount;
    public final int xSize;
    public final int ySize;
    private final double viewAngle;
    public final IPoint eyePosition;
    public final IVector lookDirection;
    private final IVector upDirection;
    private final String bufferDir;
    private final int bufferFileCount;

    public IPicture makePicture() throws IOException {
        IPicture picture;
        if (this.bufferDir != null && bufferFileCount != 0) {
            picture = new BufferedPPMPicture(
                xSize, ySize, Paths.get(this.bufferDir), xSize * ySize / this.bufferFileCount,
                (xSize, ySize) -> new PPMPicture(xSize, ySize, rayCount));
        }
        else {
            picture = new PPMPicture(xSize, ySize, rayCount);
        }
        return picture;
    }

    public Camera makeCamera() {
        return new MultipleRayCamera(this.rayCount, this.xSize, this.ySize, this.viewAngle, this.eyePosition, this.lookDirection, this.upDirection);
    }
}
