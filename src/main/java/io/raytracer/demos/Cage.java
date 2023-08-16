package io.raytracer.demos;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.Recasters;
import io.raytracer.shapes.Cylinder;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Sphere;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.MultipleRayCamera;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;


public class Cage {
    public static void main(String[] args) throws IOException {
        int firstBarPosition = -16;
        int barCount = 7;
        double barSeparation = 3;
        double barScale = 0.5;

        Material floorMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.2, 0.2, 0.2)))
                .build();
        floorMaterial.addRecaster(Recasters.diffuse, 1);
        Shape floor = new Plane(floorMaterial);

        Function<IRay, IColour> background = ray -> new LinearColour(0, 0, 0);
        LambertianWorld world = new LambertianWorld(background);
        world.put(floor);

        Material barMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.2, 0.2, 0.2)))
                .build();
        barMaterial.addRecaster(Recasters.diffuse, 1);

        Group backGroup = new Group();
        Group rightGroup = new Group();
        Group frontGroup = new Group();
        Group leftGroup = new Group();
        double barEndPosition = firstBarPosition + barSeparation*(barCount-1);
        for (int barIndex = 0; barIndex < barCount; barIndex++) {
            double barZPosition = firstBarPosition + barSeparation*barIndex;
            double barXPosition = -firstBarPosition + barZPosition;
            Shape backBar = new Cylinder(barMaterial);
            Shape rightBar = new Cylinder(barMaterial);
            Shape frontBar = new Cylinder(barMaterial);
            Shape leftBar = new Cylinder(barMaterial);
            backBar.setTransform(ThreeTransform.scaling(barScale, 1, barScale).translate(0, 0, barZPosition));
            backGroup.add(backBar);
            frontBar.setTransform(ThreeTransform.scaling(barScale, 1, barScale).translate(-firstBarPosition + barEndPosition, 0, barZPosition));
            frontGroup.add(frontBar);
            if (barIndex != 0) {
                rightBar.setTransform(ThreeTransform.scaling(barScale, 1, barScale).translate(barXPosition, 0, barEndPosition));
                leftBar.setTransform(ThreeTransform.scaling(barScale, 1, barScale).translate(barXPosition, 0, firstBarPosition));
                rightGroup.add(rightBar);
                leftGroup.add(leftBar);
            }
        }
        world.put(backGroup).put(rightGroup).put(frontGroup).put(leftGroup);

        /*
        Material smokeMaterial = Material.builder().texture(new MonocolourTexture(new LinearColour(0.8, 0.8, 0.8))).build();
        smokeMaterial.addRecaster(Recasters.diffuse, 1);
        Shape smokeBoundary1 = new Cube(smokeMaterial);
        smokeBoundary1.setTransform(ThreeTransform.scaling(100, 40, 100).translate(-60+barSeparation*(barCount-1)/2.0, 0, -60+firstBarPosition + (barSeparation*barCount-1)/2.0));
        Shape smoke1 = new Volume(0.03, smokeBoundary1, smokeMaterial);
        world.put(smoke1);
        */

        Material emitentMaterial = Material.builder().emit(new LinearColour(60, 60, 60)).build();
        Shape emitent = new Sphere(emitentMaterial);
        emitent.setTransform(ThreeTransform.scaling(7, 7, 7).translate(barSeparation*(barCount-1)/2.0, 30, firstBarPosition + (barSeparation*barCount-1)/2.0));
        world.put(emitent);

        IPoint eyePosition = new Point(45, 12, 33.915974);
        IVector lookDirection = new Point(10, 6, -5).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(500, 1024, 768, Math.PI / 4, eyePosition, lookDirection, upDirection);

        String filename = "cage.ppm";

        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}