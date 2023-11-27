package io.raytracer.demos;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.World;
import io.raytracer.tools.AveragingPPMPicture;
import io.raytracer.tools.BufferedPPMPicture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.MultipleRayCamera;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    public final double brightness;

    private Camera camera;
    @Getter private IPicture picture;

    public void render(World world) throws IOException {
        this.camera = this.makeCamera();
        IPicture picture;
        if (this.bufferDir != null && bufferFileCount != 0) {
            picture = new BufferedPPMPicture(
                xSize, ySize, Paths.get(this.bufferDir), xSize * ySize / this.bufferFileCount,
                (xSize, ySize) -> new AveragingPPMPicture(xSize, ySize, rayCount));
        }
        else {
            picture = new AveragingPPMPicture(xSize, ySize, rayCount);
        }
        world.render(picture, camera);
        this.picture = picture;
    }

    public Camera makeCamera() {
        return new MultipleRayCamera(this.rayCount, this.xSize, this.ySize, this.viewAngle, this.eyePosition, this.lookDirection, this.upDirection);
    }

    public void export() throws IOException {
        this.picture.export(Paths.get(this.filename));
    }
}
