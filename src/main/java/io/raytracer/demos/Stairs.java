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
import lombok.val;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Stairs {
    public static void main(String[] args) throws IOException {
        int xSize = 1080;
        int ySize = 1080;
        int trimSize = (int)(((float)768/1080)*ySize);
        double viewAngle = Math.PI / 3.5;
        IColour backgroundColour = new LinearColour(0, 0, 0);

        //setups
        DemoSetup verticalSetup = DemoSetup.builder()
                .rayCount(100)
                .xSize(xSize)
                .ySize(ySize)
                .viewAngle(viewAngle)
                .upDirection(new Vector(0, 0, 1))
                .eyePosition(new Point(0, 5, 1.55))
                .lookDirection(new Vector(0, -5, -1.55))
                .filename("stairsVertical.ppm")
                .build();
        List<Hittable> verticalObjects = new ArrayList<>();

        //materials
        Material keyMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.73, 0.73, 0.73)))
                .build();
        keyMaterial.addRecaster(Recasters.diffuse, 1);
        Material verticalEmitentMaterial = Material.builder().emit(new LinearColour(22, 22, 22)).build();
        //--
        Material blockMaterial = keyMaterial.toBuilder().build();
        blockMaterial.addRecaster(Recasters.diffuse, 0.7);
        blockMaterial.addRecaster(Recasters.fuzzilyReflective.apply(0.0), 0.3);

        //horizontal blocks
        Shape upperBlock = new Cube(keyMaterial);
        upperBlock.setTransform(ThreeTransform.scaling(11, 10, 0.3).translate(0, -10, 3));
        Shape lowerBlock = new Cube(keyMaterial);
        lowerBlock.setTransform(ThreeTransform.scaling(11, 20, 0.45).translate(0, -10, -2.31));
        verticalObjects.add(upperBlock);
        verticalObjects.add(lowerBlock);

        //stairs
        IVector vertDisp = new Vector(0.6, -1, 0.25);
        List<Hittable> verticalStairs = new ArrayList<>();
        ThreeTransform vertPush = ThreeTransform.scaling(1.8, 10, 0.1).translate(1 - vertDisp.x(), -10 - vertDisp.y(), -1.7 - vertDisp.z());
        for (int i = 0; i < 13; i++) {
            Shape key = new Cube(keyMaterial);
            vertPush = vertPush.translate(vertDisp.x(), vertDisp.y(), vertDisp.z()).scale(0.8, 1, 1);
            key.setTransform(vertPush);
            verticalStairs.add(key);
        }
        /*
        for (int i = 0; i < 25; i++) {
            Shape key = new Cube(keyMaterial);
            vertPush = vertPush.translate(vertDisp.x(), vertDisp.y(), vertDisp.z()).scale(0.8, 1, 1);
            vertPush = vertPush.translate(-8.39696581394432, 0 ,0)
                    .scale(Math.pow(10/0.8, 2), 1, 1)
                    .translate(8.39696581394432, 0, 0);
            key.setTransform(vertPush);
            verticalStairs.add(key);
        }
        */
        verticalObjects.add(new Group(verticalStairs.toArray(new Hittable[] {})));

        //light
        Shape verticalEmitent = new Rectangle(verticalEmitentMaterial);
        verticalEmitent.setTransform(ThreeTransform.rotation_x(Math.PI / 2 + Math.PI/6).scale(5, 1, 5).translate(0, 10, -2));
        verticalObjects.add(verticalEmitent);

        //worlds
        World verticalWorld = new LambertianWorld(backgroundColour);
        verticalWorld.put(new Group(verticalObjects.toArray(new Hittable[] {})));

        //render
        val currentSetup = verticalSetup;
        val currentWorld = verticalWorld;
        currentSetup.render(currentWorld);
        currentSetup.export();
    }
}
