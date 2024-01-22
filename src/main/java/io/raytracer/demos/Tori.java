package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.World;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Hittable;
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Torus;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;

import java.io.IOException;

public class Tori {
    private final DemoSetup setup;

    public Tori(DemoSetup setup) {
        this.setup = setup;
    }

    public void render() throws IOException {
        IColour backgroundColour = new LinearColour(0, 0, 0);

        //setups
        DemoSetup centralSetup = setup.toBuilder()
                .viewAngle(Math.PI / 4)
                .upDirection(new Vector(0, 1, 0))
                .eyePosition(new Point(0, 8, 15))
                .lookDirection(new Vector(0, -8, -15))
                .build();

        Material blockMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.65)))
                .build();

        Shape torusFlat = new Torus(4, 2, blockMaterial);
        torusFlat.setTransform(ThreeTransform.translation(-2, -3.5, -1.5));
        Shape torusStand = new Torus(3, 1, blockMaterial);
        torusStand.setTransform(ThreeTransform.rotation_x(Math.PI/2 + Math.PI/9).rotate_z(Math.PI/11).translate(3, 1, -1.5));
        Shape torusOver = new Torus(8, 2, blockMaterial);
        torusOver.setTransform(ThreeTransform.rotation_x(Math.PI / 2 - Math.PI / 6).rotate_z(Math.PI / 3).translate(-2,-6, -3));

        //light
        int lightBrightness = 25;
        Material emitentMaterial = Material.builder().emit(new LinearColour(lightBrightness)).build();
        Shape emitent = new Rectangle(emitentMaterial);
        emitent.setTransform(ThreeTransform.rotation_y(Math.PI / 2).scale(5, 5, 1));

        //worlds
        World centralWorld = new LambertianWorld(backgroundColour);
        centralWorld.put(emitent);
        centralWorld.put(new Group(new Hittable[] {torusFlat, torusOver, torusStand}));

        //render
        centralSetup.render(centralWorld);
        centralSetup.export();
    }

    public static void main(String[] args) throws IOException {
        int size = 1080;
        int rayCount = 5;
        String filenameTemplate = "./outputs/tori/em/em10.ppm";

        DemoSetup setup = DemoSetup.builder()
                .rayCount(rayCount)
                .xSize(size)
                .ySize(size)
                .filename(filenameTemplate)
                .build();
        Tori renderer = new Tori(setup);
        renderer.render();
    }
}
