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
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.MultipleRayCamera;
import io.raytracer.tools.PPMPicture;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Pillars {
    public static void main(String[] args) throws IOException {
        Function<IRay, IColour> background = ray -> new GammaColour(0, 0, 0);
        LambertianWorld world = new LambertianWorld(background);

        Material lighting = Material.builder().emit(new GammaColour(10, 10, 10)).build();
        Shape light = new Rectangle(lighting);
        light.setTransform(ThreeTransform.scaling(25, 25, 1).rotate_x(Math.PI / 2).translate(0, 35, -10));
        world.put(light);

        Material cylinderMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.6, 0.6, 0.6)))
                .build();
        cylinderMaterial.addRecaster(Recasters.diffuse, 1);

        /*
        int pillarsCount = 25;
        Random randGen = new Random(1256);
        Point[] pillarPositions = IntStream.range(0, pillarsCount)
            .mapToObj(i -> new Point(20 - randGen.nextInt(40), 3 - randGen.nextInt(6), 20 - randGen.nextInt(40)))
            .toArray(Point[]::new);
         */
        Point[] prePillarPositions = new Point[] {
            new Point(-9.0, 3.0, 7.0),
            new Point(7.0, -2.0, 11.0),
            new Point(15.0, 1.0, -13.0),
            new Point(2.0, -1.0, -12.0),
            new Point(-15.0, 2.0, 9.0),
            new Point(-3.0, 2.0, 12.0),
            new Point(-13.0, 2.0, -4.0),
            new Point(11.0, 1.0, -8.0),
            new Point(-3.0, 0.0, 12.0),
            new Point(-16.0, -2.0, -11.0),
            new Point(2.0, 2.0, -3.0),
            new Point(9.0, 3.0, 3.0),
            new Point(9.0, 0.0, -1.0),
            new Point(-9.0, -2.0, 5.0),
            new Point(-2.0, 3.0, -3.0),
            new Point(-1.0, 0.0, 1.0),
            new Point(8.0, 2.0, -12.0),
            new Point(-9.0, -1.0, -6.0),
            new Point(17.0, 0.0, -8.0),
            new Point(13.0, -2.0, 4.0),
            new Point(11.0, 3.0, 10.0),
            new Point(3.0, 2.0, 8.0),
            new Point(8.0, -1.0, 8.0),
            new Point(-2.0, 2.0, -14.0),
            new Point(-11.0, 0.0, 10.0)
        };
        Point[] pillarPositions = Arrays.stream(prePillarPositions).map(p -> new Point(1.2*p.get(0), 1.2*p.get(1), 1.2*p.get(2))).toArray(Point[]::new);
        IntStream.range(0, pillarPositions.length).forEach(positionIndex -> {
            Cylinder pillar = new Cylinder(cylinderMaterial);
            Point currentPosition = pillarPositions[positionIndex];
            pillar.setUpperBound(2 + currentPosition.get(1));
            pillar.setLowerBound(-100);
            pillar.setTopClosed(true);
            pillar.setTransform(ThreeTransform.scaling(2, 2, 2).translate(currentPosition.get(0), 0, currentPosition.get(2)));
            world.put(pillar);
        });

        IPoint eyePosition = new Point(-2, 30, 15);
        IVector lookDirection = new Point(-2, -3, -2).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(400, 1080, 1080, Math.PI / 4, eyePosition, lookDirection, upDirection);

        String filename = "pillars.ppm";
        IPicture picture = new PPMPicture(1080, 1080);
        world.render(picture, camera);
        picture.export(Paths.get(filename));
    }
}