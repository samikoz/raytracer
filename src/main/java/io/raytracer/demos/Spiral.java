package io.raytracer.demos;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.Recasters;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Hittable;
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.readymades.SpiralParameters;
import io.raytracer.shapes.readymades.SpiralStairs;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.LinearColour;

import java.io.IOException;

public class Spiral {
    public static void main(String[] args) throws IOException {
        int size = 1080;
        int lightIntensity = 30;
        LambertianWorld world = new LambertianWorld();

        IPoint eyePosition = new Point(0, -5, -40);
        DemoSetup setup = DemoSetup.builder()
                .rayCount(400)
                .xSize(size)
                .ySize(size)
                .viewAngle(Math.PI / 4)
                .upDirection(new Vector(0, 1, 0))
                .eyePosition(eyePosition)
                .lookDirection(new Vector(0, 0, 1))
                .filename("spiral.ppm")
                .bufferDir("./buffSpiral/")
                .bufferFileCount(20)
                .build();

        Material stairMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.73, 0.73, 0.73)))
                .build();
        stairMaterial.addRecaster(Recasters.diffuse, 1);

        SpiralParameters parameters = SpiralParameters.builder()
                .count(15)
                .radius(10)
                .stairDrop(1)
                .stairHeight(10)
                .stairDepth(1)
                .stairWidth(2)
                .stairSeparation(0.1)
                .stairMaterial(stairMaterial)
                .centre(new Point(0, 0, 0))
                .orientation(false)
                .build();

        Material lighting = Material.builder().emit(new GammaColour(lightIntensity, lightIntensity, lightIntensity)).build();
        Shape light = new Rectangle(lighting);
        light.setTransform(ThreeTransform.scaling(10, 10, 11).translate(0, 22, 0));

        Group stairs = SpiralStairs.form(parameters);
        Group all = new Group(new Hittable[]{stairs, light});
        world.put(all);

        setup.render(world);
        setup.export();
    }
}