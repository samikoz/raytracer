package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.World;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Paths;

@AllArgsConstructor
public class RoundObserver {
    private final double radius;
    private final String name;

    public void observe(World world, double angle, int count) throws IOException {
        Camera camera = this.getFrontCamera();

        for (int i = 0; i < count; i++) {
            camera.transform(ThreeTransform.rotation_y(i*angle));
            world.render(camera).export(Paths.get(String.format("%s_%d.ppm", this.name, i)));
        }
    }

    private Camera getFrontCamera() {
        IPoint eyePosition = new Point(this.radius, 2, 0);
        return new MultipleRayCamera(4, 600, 300, Math.PI/3, eyePosition, new Point(0, 0, 0).subtract(eyePosition), new Vector(0, 1, 0));
    }
}
