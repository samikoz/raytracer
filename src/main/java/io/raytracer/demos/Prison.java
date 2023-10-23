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
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.MultipleRayCamera;
import io.raytracer.tools.PPMPicture;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Function;

public class Prison {
    public static void main(String[] args) throws IOException {
        int barCount = 19;
        double barScale = 0.2;
        double vBarSeparation = 8;

        Material floorMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.2, 0.2, 0.2)))
                .build();
        floorMaterial.addRecaster(Recasters.diffuse, 1);
        Shape floor = new Plane(floorMaterial);
        Shape wall = new Plane(floorMaterial);
        wall.setTransform(ThreeTransform.rotation_z(Math.PI/2).rotate_y(Math.PI/2).translate(0, 0, 30));

        Material emitent = Material.builder().emit(new LinearColour(5, 5, 5)).build();
        Shape rect = new Rectangle(emitent);
        rect.setTransform(ThreeTransform.scaling(barCount/2, 5, 1).rotate_y(Math.PI/2).rotate_z(-Math.PI/3).translate(-5, 25, barCount/2));

        /*
        Material smokeMaterial = Material.builder().texture(new MonocolourTexture(new LinearColour(1, 1, 1))).build();
        smokeMaterial.addRecaster(Recasters.isotropic, 1);
        Shape smokeBoundary = new Cube();
        smokeBoundary.setTransform(ThreeTransform.scaling(40, 1, 46).translate(-16, 0, -23));
        Shape smoke = new Volume(0.01, smokeBoundary, smokeMaterial);
        */

        Function<IRay, IColour> background = ray -> new LinearColour(0, 0, 0);
        LambertianWorld world = new LambertianWorld(background);
        world.put(floor).put(rect).put(wall);
        //world.put(smoke);

        Material barMaterial = Material.builder()
            .texture(new MonocolourTexture(new LinearColour(0.8, 0.8, 0.8)))
            .build();
        barMaterial.addRecaster(Recasters.fuzzilyReflective.apply(0.2), 1);
        for (int barIndex = 0; barIndex < barCount; barIndex++) {
            double barZPosition = -16 + barIndex*2.5;
            Shape bar = new Cylinder(barMaterial);
            bar.setTransform(ThreeTransform.scaling(barScale, 1, barScale).translate(0, 0, barZPosition));
            world.put(bar);
        }
        for (int vBarIndex = 0; vBarIndex < 2; vBarIndex++) {
            double barYPosition = vBarSeparation*(1 + vBarIndex);
            Shape vBar = new Cylinder(barMaterial);
            vBar.setTransform(ThreeTransform.scaling(barScale, 1, barScale).rotate_x(Math.PI / 2).translate(0, barYPosition, 0));
            world.put(vBar);
        }

        IPoint eyePosition = new Point(35, 15, -22);
        IVector lookDirection = new Point(5, 7, 10).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(400, 1024, 768, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "prison.ppm";
        IPicture picture = new PPMPicture(1024, 768);
        world.render(picture, camera);
        picture.export(Paths.get(filename));
    }
}