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
import io.raytracer.textures.CheckerTexture;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.Texture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.MultipleRayCamera;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Prison {
    public static void main(String[] args) throws IOException {
        int columnCount = 8;
        int columnSeparation = 22;
        double columnScale = 2;

        Function<IRay, IColour> background = ray -> new GammaColour(0, 0, 0);
        LambertianWorld world = new LambertianWorld(background);

        Texture floorTexture = new CheckerTexture(new GammaColour(0, 0, 0), new GammaColour(1, 1, 1));
        floorTexture.setTransform(ThreeTransform.scaling(2, 2, 2));
        Material floorMaterial = Material.builder()
            .texture(floorTexture)
            .build();
        floorMaterial.addRecaster(Recasters.diffuse, 1);
        Shape floor = new Plane(floorMaterial);
        world.put(floor);

        Material columnMaterial = Material.builder()
            .texture(new MonocolourTexture(new GammaColour(0.2, 0.2, 0.2)))
            .build();
        columnMaterial.addRecaster(Recasters.diffuse, 1);
        IntStream.range(0, columnCount).mapToObj(columnIndex -> {
            Shape cylinder = new Cylinder(columnMaterial);
            cylinder.setTransform(ThreeTransform.scaling(columnScale, 1, columnScale).translate(15-columnIndex*columnSeparation, 0, 0));
            return cylinder;
        }).forEach(world::put);
        IntStream.range(0, columnCount).mapToObj(columnIndex -> {
            Shape cylinder = new Cylinder(columnMaterial);
            cylinder.setTransform(ThreeTransform.scaling(columnScale, 1, columnScale).translate(15-columnIndex*columnSeparation, 0, 25));
            return cylinder;
        }).forEach(world::put);
        IntStream.range(0, columnCount).mapToObj(columnIndex -> {
            Shape cylinder = new Cylinder(columnMaterial);
            cylinder.setTransform(ThreeTransform.scaling(columnScale, 1, columnScale).translate(15-columnIndex*columnSeparation, 0, 50));
            return cylinder;
        }).forEach(world::put);

        Material emitent = Material.builder().emit(new GammaColour(64, 64, 64)).build();
        Shape lighting = new Rectangle(emitent);
        lighting.setTransform(ThreeTransform.scaling(5, 5, 1).translate(10, 10, -6));
        world.put(lighting);


        IPoint eyePosition = new Point(45, 12, 40);
        IVector lookDirection = new Point(-15, 2, 25).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(200, 1024, 768, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "prison_3.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}