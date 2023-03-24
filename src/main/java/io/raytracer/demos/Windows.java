package io.raytracer.demos;

import io.raytracer.drawing.Camera;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.IPicture;
import io.raytracer.drawing.PPMPicture;
import io.raytracer.drawing.patterns.CheckerPattern;
import io.raytracer.drawing.patterns.Monopattern;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.worldly.IWorld;
import io.raytracer.worldly.LightSource;
import io.raytracer.worldly.World;
import io.raytracer.worldly.drawables.Drawable;
import io.raytracer.worldly.drawables.Plane;
import io.raytracer.worldly.drawables.Sphere;
import io.raytracer.worldly.materials.Material;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Windows {
    public static void main(String[] args) throws IOException {
        int spheresCount = 10;
        int sphereSeparation = 10;

        IWorld world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(-20, 10, -10)));

        Material floorMaterial = Material.builder()
                .pattern(new CheckerPattern(new Colour(0, 0, 0), new Colour(1, 1, 1)))
                .specular(0.0)
                .ambient(0.1)
                .diffuse(0.9)
                .shininess(200.0)
                .reflectivity(0.5)
                .build();
        Plane floor = new Plane(floorMaterial);
        world.put(floor);

        Material.MaterialBuilder sphereMaterialBuilder = Material.builder()
                .pattern(new Monopattern(new Colour(1, 0, 0)))
                .specular(0.3).ambient(0.1).shininess(100.0);

        for (int sphereIndex = 0; sphereIndex < spheresCount; sphereIndex++) {
            Material sphereMaterial = sphereMaterialBuilder.diffuse(0.7 - 0.35*(sphereIndex/(float)spheresCount)).build();
            Drawable aSphere = new Sphere(sphereMaterial);
            aSphere.setTransform(ThreeTransform.translation(0, 1, sphereSeparation*sphereIndex));
            world.put(aSphere);
        }

        IPoint leftEyePosition = new Point(-5, 1, -10);
        IVector leftLookDirection = new Point(0, 0, 5*sphereSeparation).subtract(leftEyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        ICamera leftCamera = new Camera(600, 600, Math.PI / 3, leftEyePosition, leftLookDirection, upDirection);

        IPoint rightEyePosition = new Point(5, 1, -10);
        IVector rightLookDirection = new Point(0, 0, 5*sphereSeparation).subtract(rightEyePosition);
        ICamera rightCamera = new Camera(600, 600, Math.PI / 3, rightEyePosition, rightLookDirection, upDirection);

        String filename = "windows.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture leftPicture = world.render(leftCamera);
        IPicture rightPicture = world.render(rightCamera);

        IPicture finalPicture = new PPMPicture(1250, 600);
        finalPicture.embed(leftPicture, 0, 0);
        finalPicture.embed(rightPicture, 650, 0);
        finalPicture.export(writer);
    }
}
