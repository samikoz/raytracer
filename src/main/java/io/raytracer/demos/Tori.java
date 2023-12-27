package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.Recasters;
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
    public static void main(String[] args) throws IOException {
        int size = 1080;
        IColour backgroundColour = new LinearColour(0, 0, 0);

        //setups
        DemoSetup centralSetup = DemoSetup.builder()
                .rayCount(1500)
                .xSize(size)
                .ySize(size)
                .viewAngle(Math.PI / 4)
                .upDirection(new Vector(0, 1, 0))
                .eyePosition(new Point(0, 8, 15))
                .lookDirection(new Vector(0, -8, -15))
                .filename("outputs/insideTorus.ppm")
                .build();

        Material blockMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.65)))
                .build();
        blockMaterial.addRecaster(Recasters.diffuse, 1);

        Shape torusFlat = new Torus(3, 1, blockMaterial);
        torusFlat.setTransform(ThreeTransform.translation(-2.5, -2.5, -1.5));
        Shape torusStand = new Torus(3, 1, blockMaterial);
        torusStand.setTransform(ThreeTransform.rotation_x(Math.PI / 2).rotate_z(Math.PI / 4).translate(-0.2, -0.2, -1.5));

        //light
        int lightBrightness = 25;
        Material emitentMaterial = Material.builder().emit(new LinearColour(lightBrightness)).build();
        Shape emitent = new Rectangle(emitentMaterial);
        emitent.setTransform(ThreeTransform.rotation_y(Math.PI / 2).scale(5, 5, 1));

        //worlds
        World centralWorld = new LambertianWorld(backgroundColour);
        centralWorld.put(emitent);
        centralWorld.put(new Group(new Hittable[] {torusFlat, torusStand}));

        //render
        centralSetup.render(centralWorld);
        centralSetup.export();
    }
}
