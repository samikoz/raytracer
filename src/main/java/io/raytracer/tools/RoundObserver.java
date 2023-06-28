package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.World;
import lombok.AllArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@AllArgsConstructor
public class RoundObserver {
    private final double radius;
    private final String name;

    public void observe(World world) throws IOException {
        Camera camera = this.getFrontCamera();

        PrintWriter frontWriter = new PrintWriter(new FileWriter(String.format("%s_front.ppm", this.name)));
        world.render(camera).export(frontWriter);

        camera.transform(ThreeTransform.rotation_z(-Math.PI/2));
        PrintWriter rightWriter = new PrintWriter(new FileWriter(String.format("%s_right.ppm", this.name)));
        world.render(camera).export(rightWriter);

        camera.transform(ThreeTransform.rotation_z(-Math.PI/2));
        PrintWriter backWriter = new PrintWriter(new FileWriter(String.format("%s_back.ppm", this.name)));
        world.render(camera).export(backWriter);

        camera.transform(ThreeTransform.rotation_z(-Math.PI/2));
        PrintWriter leftWriter = new PrintWriter(new FileWriter(String.format("%s_left.ppm", this.name)));
        world.render(camera).export(leftWriter);
    }

    private Camera getFrontCamera() {
        IPoint eyePosition = new Point(this.radius, 0, 0);
        return new SingleRayCamera(300, 300, Math.PI/3, eyePosition, new Point(0, 0, 0).subtract(eyePosition), new Vector(0, 0, 1));
    }
}
