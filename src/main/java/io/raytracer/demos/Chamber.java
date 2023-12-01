package io.raytracer.demos;

import io.raytracer.shapes.Plane;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.Recasters;
import io.raytracer.shapes.Cylinder;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Sphere;
import io.raytracer.textures.CheckerTexture;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.Texture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.MultipleRayCamera;
import io.raytracer.tools.PPMPicture;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Chamber {
    public static void main(String[] args) throws IOException {
        int columnCount = 7;
        int columnSeparation = 35;
        double columnScale = 3;
        int raysCount = Integer.parseInt(args[0]);

        Function<IRay, IColour> background = ray -> new GammaColour(0, 0, 0);
        LambertianWorld world = new LambertianWorld(background);

        Texture floorTexture = new CheckerTexture(new GammaColour(0, 0, 0), new GammaColour(1, 1, 1));
        floorTexture.setTransform(ThreeTransform.scaling(2, 2, 2));
        Material floorMaterial = Material.builder()
            .texture(floorTexture)
            .build();
        floorMaterial.addRecaster(Recasters.diffuse, 1);
        Shape floor = new Plane(new Vector(0, 1, 0), floorMaterial);
        world.put(floor);

        Material columnMaterial = Material.builder()
            .texture(new MonocolourTexture(new GammaColour(0.2, 0.2, 0.2)))
            .build();
        columnMaterial.addRecaster(Recasters.diffuse, 1);
        /*IntStream.range(0, columnCount).mapToObj(columnIndex -> {
            Shape cylinder = new Cylinder(columnMaterial);
            cylinder.setTransform(ThreeTransform.scaling(columnScale, 1, columnScale).translate(15-columnIndex*columnSeparation, 0, 0));
            return cylinder;
        }).forEach(world::put);
        IntStream.range(0, columnCount).mapToObj(columnIndex -> {
            Shape cylinder = new Cylinder(columnMaterial);
            cylinder.setTransform(ThreeTransform.scaling(columnScale, 1, columnScale).translate(15-columnIndex*columnSeparation, 0, 25));
            return cylinder;
        }).forEach(world::put);*/
        IntStream.range(0, columnCount).mapToObj(columnIndex -> {
            Shape cylinder = new Cylinder(columnMaterial);
            cylinder.setTransform(ThreeTransform.scaling(columnScale, 1, columnScale).translate(15-columnIndex*columnSeparation, 0, 50));
            return cylinder;
        }).forEach(world::put);

        Material emitent = Material.builder().emit(new GammaColour(200, 200, 200)).build();
        Shape lighting = new Sphere(emitent);
        lighting.setTransform(ThreeTransform.scaling(5, 5, 5).translate(-5, 10, 80));
        world.put(lighting);


        IPoint eyePosition = new Point(55, 12, 30);
        IVector lookDirection = new Point(-15, 8, 25).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(1000, 1080, 1080, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = String.format("chamber_%3d.ppm", raysCount);
        IPicture picture = new PPMPicture(1080, 1080);
        world.render(picture, camera);
        picture.export(Paths.get(filename));
    }
}