package io.raytracer.demos;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.World;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.MultipleRayCamera;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;


@Builder(toBuilder = true)
@RequiredArgsConstructor
public class DemoSetup {
    private final int rayCount;
    private final int xSize;
    private final int ySize;
    private final double viewAngle;
    private final IPoint eyePosition;
    private final IVector lookDirection;
    private final IVector upDirection;
    private final String filename;

    public IPicture render(World world) {
        Camera cameraBottom = new MultipleRayCamera(this.rayCount, this.xSize, this.ySize, this.viewAngle, this.eyePosition, this.lookDirection, this.upDirection);
        return world.render(cameraBottom);
    }

    public Path getPath() {
        return Paths.get(this.filename);
    }
}
