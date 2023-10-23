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
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import lombok.val;

import java.io.IOException;


public class Stairs {
    public static void main(String[] args) throws IOException {
        int xSize = 1080;
        int ySize = 1080;
        int trimSize = (int)(((float)768/1080)*ySize);
        double viewAngle = Math.PI / 4;
        IColour backgroundColour = new LinearColour(0, 0, 0);

        //setups
        DemoSetup horizontalSetup = DemoSetup.builder()
                .rayCount(250)
                .xSize(xSize)
                .ySize(ySize)
                .viewAngle(viewAngle)
                .upDirection(new Vector(0, 0, -1))
                .eyePosition(new Point(0, 5, 1))
                .lookDirection(new Vector(0, -5, -1))
                .filename("stairsHorizontal.ppm")
                .build();
        //--
        DemoSetup verticalSetup = horizontalSetup.toBuilder()
                .rayCount(300)
                .eyePosition(new Point(0, 5, 1.55))
                .lookDirection(new Vector(0, -5, -1.55))
                .filename("stairsVertical.ppm")
                .build();

        //materials
        Material blockMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.6, 0.6, 0.6)))
                .build();
        blockMaterial.addRecaster(Recasters.diffuse, 1);
        Material emitentMaterial = Material.builder().emit(new LinearColour(10, 10, 10)).build();
        Material verticalEmitentMaterial = Material.builder().emit(new LinearColour(15, 15, 15)).build();

        //horizontal blocks
        Shape horizontalLowerBlock = new Cube(blockMaterial);
        horizontalLowerBlock.setTransform(ThreeTransform.scaling(11, 10, 0.3).translate(0, -10, 1.78));
        Shape horizontalUpperBlock = new Cube(blockMaterial);
        horizontalUpperBlock.setTransform(ThreeTransform.scaling(11, 10, 0.37).translate(0, -10, -2.04));
        //--
        Shape verticalLowerBlock = new Cube(blockMaterial);
        verticalLowerBlock.setTransform(ThreeTransform.scaling(11, 10, 0.3).translate(0, -10, 1.85));
        Shape verticalUpperBlock = new Cube(blockMaterial);
        verticalUpperBlock.setTransform(ThreeTransform.scaling(11, 10, 0.45).translate(0, -10, -2.31));

        //stairs
        IVector horDisp = new Vector(0.6, -1, 0);
        Group horizontalKeys = new Group();
        ThreeTransform horPush = ThreeTransform.scaling(0.2, 10, 0.6).translate(-1.9 - horDisp.get(0), -10 - horDisp.get(1), -1 - horDisp.get(2));
        for (int i = 0; i < 20; i++) {
            Shape key = new Cube(blockMaterial);
            horPush = horPush.translate(horDisp.get(0), horDisp.get(1), horDisp.get(2));
            key.setTransform(horPush);
            horizontalKeys.add(key);
        }
        //--
        IVector vertDisp = new Vector(0.6, -1, 0.25);
        Group verticalKeys = new Group();
        ThreeTransform vertPush = ThreeTransform.scaling(1.8, 10, 0.1).translate(1 - vertDisp.get(0), -10 - vertDisp.get(1), -1.7 - vertDisp.get(2));
        for (int i = 0; i < 13; i++) {
            Shape key = new Cube(blockMaterial);
            vertPush = vertPush.translate(vertDisp.get(0), vertDisp.get(1), vertDisp.get(2)).scale(0.8, 1, 1);
            key.setTransform(vertPush);
            verticalKeys.add(key);
        }

        //light
        Shape emitent = new Rectangle(emitentMaterial);
        emitent.setTransform(ThreeTransform.rotation_x(Math.PI / 2).scale(5, 1, 5).translate(0, 10, -2));
        //--
        Shape verticalEmitent = new Rectangle(verticalEmitentMaterial);
        verticalEmitent.setTransform(ThreeTransform.rotation_x(Math.PI / 2).scale(5, 1, 5).translate(0, 10, -2));

        //worlds
        World horizontalWorld = new LambertianWorld(backgroundColour);
        horizontalWorld.put(horizontalLowerBlock).put(horizontalUpperBlock);
        horizontalWorld.put(horizontalKeys);
        horizontalWorld.put(emitent);
        //--
        World verticalWorld = new LambertianWorld(backgroundColour);
        verticalWorld.put(verticalLowerBlock).put(verticalUpperBlock);
        verticalWorld.put(verticalKeys);
        verticalWorld.put(verticalEmitent);

        //render
        val currentSetup = horizontalSetup;
        val currentWorld = horizontalWorld;
        IPicture picture = currentSetup.render(currentWorld);

        /*
        IPicture red = new PPMPicture(xSize, ySize);
        red.fill(new LinearColour(1, 0, 0));
        picture.embed(red, (x, y) -> x < xSize/2 && (y == (ySize - trimSize)/2 || y == (ySize + trimSize)/2));
         */

        picture.export(currentSetup.getPath());
    }
}
