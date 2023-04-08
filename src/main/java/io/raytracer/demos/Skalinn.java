package io.raytracer.demos;

import io.raytracer.tools.ICamera;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.Colour;
import io.raytracer.textures.CheckerTexture;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Sphere;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.LightSource;
import io.raytracer.mechanics.IWorld;
import io.raytracer.mechanics.World;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Skalinn {
    public static void main(String[] args) throws IOException {
        int spheresCount = 10;
        int sphereSeparation = 10;

        IWorld world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(-20, 10, -10)));

        Material floorMaterial = Material.builder()
                .texture(new CheckerTexture(new Colour(0, 0, 0), new Colour(1, 1, 1)))
                .specular(0.0)
                .ambient(0.1)
                .diffuse(0.9)
                .shininess(200.0)
                .reflectivity(0.5)
                .build();
        Plane floor = new Plane(floorMaterial);
        world.put(floor);

        Material.MaterialBuilder sphereMaterialBuilder = Material.builder()
                .texture(new MonocolourTexture(new Colour(1, 0, 0)))
                .specular(0.3).ambient(0.1).shininess(100.0);

        for (int sphereIndex = 0; sphereIndex < spheresCount; sphereIndex++) {
            Material sphereMaterial = sphereMaterialBuilder.diffuse(0.7 - 0.35*(sphereIndex/(float)spheresCount)).build();
            Shape aSphere = new Sphere(sphereMaterial);
            aSphere.setTransform(ThreeTransform.translation(0, 1, sphereSeparation*sphereIndex));
            world.put(aSphere);
        }

        IPoint eyePosition = new Point(-5, 1, -5);
        IVector lookDirection = new Point(0, 0, 5*sphereSeparation).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        ICamera camera = new Camera(1200, 900, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "skalinn.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}
