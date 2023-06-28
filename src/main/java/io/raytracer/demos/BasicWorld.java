package io.raytracer.demos;

import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.shapes.Cylinder;
import io.raytracer.shapes.Shape;
import io.raytracer.tools.Camera;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.MultipleRayCamera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Sphere;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

public class BasicWorld {
    public static void main(String[] args) throws IOException {
        Material sphereMaterial = Material.builder()
                .texture(new MonocolourTexture(new GammaColour(0.5, 0.5, 0.5)))
                .build();
        Shape floor = new Sphere(sphereMaterial);
        floor.setTransform(ThreeTransform.scaling(100, 100, 100).translate(0, -100.5, -4));

        Shape sphere = new Sphere(sphereMaterial);
        sphere.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5).translate(0, 0, -4));

        Function<IRay, IColour> background = ray -> {
            double t = 0.5*(ray.getDirection().normalise().get(1) + 1);
            return (new GammaColour(1, 1, 1)).interpolate(new GammaColour(0.5, 0.7, 1.0), t);
        };
        LambertianWorld world = new LambertianWorld(background);
        world.put(floor).put(sphere);

        IPoint eyePosition = new Point(0, 0.5, 0);
        IVector lookDirection = new Point(0, 0, -4).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(50, 600, 300, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "basic_world.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}