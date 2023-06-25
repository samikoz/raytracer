package io.raytracer.demos;

import io.raytracer.tools.Camera;
import io.raytracer.tools.Colour;
import io.raytracer.tools.ICamera;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.PPMPicture;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.World;
import io.raytracer.mechanics.LightSource;
import io.raytracer.mechanics.PhongWorld;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Sphere;
import io.raytracer.materials.Material;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Windows {
    public static void main(String[] args) throws IOException {
        int spheresCount = 10;
        int sphereSeparation = 10;

        PhongWorld world = new PhongWorld();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(0, 10, -30)));

        Material floorMaterial = Material.builder()
                .reflectivity(0.8)
                .transparency(0.1)
                .build();
        Plane floor = new Plane(floorMaterial);
        world.put(floor);

        Material.MaterialBuilder sphereMaterialBuilder = Material.builder()
                .texture(new MonocolourTexture(new Colour(0, 1, 0)))
                .specular(0.3).ambient(0.1).shininess(150.0);

        for (int sphereIndex = 0; sphereIndex < spheresCount; sphereIndex++) {
            Material sphereMaterial = sphereMaterialBuilder.diffuse(0.7*(1 - (sphereIndex/(float)spheresCount))).build();
            Shape aSphere = new Sphere(sphereMaterial);
            aSphere.setTransform(ThreeTransform.translation(0, 1, sphereSeparation*sphereIndex));
            world.put(aSphere);
        }

        IPoint leftEyePosition = new Point(-3, 1, -10);
        IVector leftLookDirection = new Point(0, 0, 5*sphereSeparation).subtract(leftEyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        ICamera leftCamera = new Camera(600, 600, Math.PI / 3, leftEyePosition, leftLookDirection, upDirection);

        IPoint rightEyePosition = new Point(3, 1, -10);
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
