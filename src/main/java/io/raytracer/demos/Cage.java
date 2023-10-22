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
import io.raytracer.shapes.Disc;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Sphere;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.MultipleRayCamera;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Function;


public class Cage {
    public static void main(String[] args) throws IOException {
        int firstBarPosition = -16;
        int barCount = 5;
        double barSeparation = 3;
        double barScale = 0.5;
        int raysCount = Integer.parseInt(args[0]);

        Material floorMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.2, 0.2, 0.2)))
                .build();
        floorMaterial.addRecaster(Recasters.diffuse, 1);
        Shape floor = new Disc(floorMaterial);
        floor.setTransform(ThreeTransform.rotation_x(Math.PI / 2).scale(27, 27, 27).translate(barSeparation*(barCount-1)/2.0, 0, firstBarPosition + (barSeparation*barCount-1)/2.0));

        Function<IRay, IColour> background = ray -> new LinearColour(0, 0, 0);
        LambertianWorld world = new LambertianWorld(background);

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
            Cylinder backBar = new Cylinder(barMaterial);
            backBar.setLowerBound(0);
            Cylinder rightBar = new Cylinder(barMaterial);
            rightBar.setLowerBound(0);
            Cylinder frontBar = new Cylinder(barMaterial);
            frontBar.setLowerBound(0);
            Cylinder leftBar = new Cylinder(barMaterial);
            leftBar.setLowerBound(0);
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

        /*
        Material smokeMaterial = Material.builder().texture(new MonocolourTexture(new LinearColour(0.8, 0.8, 0.8))).build();
        smokeMaterial.addRecaster(Recasters.diffuse, 1);
        Shape smokeBoundary1 = new Cube(smokeMaterial);
        smokeBoundary1.setTransform(ThreeTransform.scaling(100, 40, 100).translate(-60+barSeparation*(barCount-1)/2.0, 0, -60+firstBarPosition + (barSeparation*barCount-1)/2.0));
        Shape smoke1 = new Volume(0.03, smokeBoundary1, smokeMaterial);
        world.put(smoke1);
        */

        Material emitentMaterial = Material.builder().emit(new LinearColour(40, 40, 40)).build();
        Shape emitent = new Sphere(emitentMaterial);
        emitent.setTransform(ThreeTransform.scaling(4, 4, 4).translate(barSeparation*(barCount-1)/2.0, 30, firstBarPosition + (barSeparation*barCount-1)/2.0));

        Group all = new Group();
        all.setTransform(ThreeTransform.translation(0, 12, 0));
        all.add(backGroup).add(leftGroup).add(frontGroup).add(rightGroup).add(floor).add(emitent);
        world.put(all);

        IPoint eyePosition = new Point(30, 15, 17.915974).multiply(2);
        IVector lookDirection = new Vector(-35, -18, -39);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(raysCount, 1080, 1080, Math.PI / 4, eyePosition, lookDirection, upDirection);

        //put RadialGradient for the disc's texture, maybe adjust it so that in the middle square the color is constant
        //lower everything so that when we add white stripes the effect is as the present one
        String filename = String.format("cage_%3d.ppm", raysCount);

        IPicture picture = world.render(camera);
        picture.export(Paths.get(filename));
    }
}