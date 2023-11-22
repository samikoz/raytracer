package io.raytracer.demos;

import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.Recasters;
import io.raytracer.mechanics.World;
import io.raytracer.shapes.Cube;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Hittable;
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Stairs {
    public static void main(String[] args) throws IOException {
        int size = 1080;
        IColour backgroundColour = new LinearColour(0, 0, 0);

        //setups
        DemoSetup horizontalSetup = DemoSetup.builder()
                .rayCount(200)
                .xSize(size)
                .ySize(size)
                .viewAngle(Math.PI / 4)
                .upDirection(new Vector(0, 0, -1))
                .eyePosition(new Point(0, 5, 1))
                .lookDirection(new Vector(0, -5, -1))
                .filename("bare.ppm")
                .build();
        //--
        DemoSetup centralSetup = horizontalSetup.toBuilder()
                .build();

        //materials
        Material blockMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.6, 0.6, 0.6)))
                .build();
        blockMaterial.addRecaster(Recasters.diffuse, 1);
        Material emitentMaterial = Material.builder().emit(new LinearColour(10, 10, 10)).build();

        //blocks
        Shape horizontalLowerBlock = new Cube(blockMaterial);
        horizontalLowerBlock.setTransform(ThreeTransform.scaling(11, 30, 0.3).translate(0, 10, 1.78));
        Shape horizontalUpperBlock = new Cube(blockMaterial);
        horizontalUpperBlock.setTransform(ThreeTransform.scaling(11, 15, 0.37).translate(0, -5, -2.04));
        //--
        Shape centralLowerBlock = new Cube(blockMaterial);
        centralLowerBlock.setTransform(ThreeTransform.scaling(11, 10, 0.3).translate(0, -10, 1.78));
        Shape centralUpperBlock = new Cube(blockMaterial);
        centralUpperBlock.setTransform(ThreeTransform.scaling(11, 10, 0.37).translate(0, -10, -2.04));

        //stairs
        IVector horDisp = new Vector(0.6, -1, 0);
        List<Hittable> horizontalKeys = new ArrayList<>();
        ThreeTransform horPush = ThreeTransform.scaling(0.2, 10, 0.6).translate(-1.9 - horDisp.x(), -10 - horDisp.y(), -1 - horDisp.z());
        for (int i = 0; i < 20; i++) {
            Shape key = new Cube(blockMaterial);
            horPush = horPush.translate(horDisp.x(), horDisp.y(), horDisp.z());
            key.setTransform(horPush);
            horizontalKeys.add(key);
        }

        //light
        Shape emitent = new Rectangle(emitentMaterial);
        emitent.setTransform(ThreeTransform.rotation_x(Math.PI / 2).scale(5, 1, 5).translate(0, 10, -2));

        //worlds
        Group steps = new Group(horizontalKeys);
        World world = new LambertianWorld(backgroundColour);
        world.put(emitent).put(horizontalUpperBlock).put(horizontalLowerBlock);
        world.put(steps);

        //render
        horizontalSetup.render(world);
        horizontalSetup.export();
    }
}
