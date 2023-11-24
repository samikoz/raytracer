package io.raytracer.demos;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.Recasters;
import io.raytracer.mechanics.World;
import io.raytracer.shapes.Cube;
import io.raytracer.shapes.CubeCorner;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Hittable;
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.Pixel;
import lombok.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Stairs {
    public static void main(String[] args) throws IOException {
        int size = 1080;
        @NonNull String name = args[0];
        @NonNull String name_appendix = args[1];
        float brightness = Float.parseFloat(args[2]);
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
        //to try out:
        DemoSetup centralSetup = horizontalSetup.toBuilder()
                .upDirection(new Vector(0, 0, 1))
                .eyePosition(new Point(0, 5, -0.2))
                .lookDirection(new Vector(0, -5, 0))
                .filename(String.format("%s%s.ppm", name, name_appendix))
                .bufferDir(String.format("./buffs/buff%s_%s/", name, name_appendix))
                .bufferFileCount(5)
                .brightness(brightness)
                .build();

        //materials
        Material blockMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(centralSetup.brightness)))
                .build();
        blockMaterial.addRecaster(Recasters.diffuse, 1);
        Material emitentMaterial = Material.builder().emit(new LinearColour(10, 10, 10)).build();

        //blocks
        Shape horizontalLowerBlock = new Cube(blockMaterial);
        horizontalLowerBlock.setTransform(ThreeTransform.scaling(11, 30, 0.3).translate(0, 10, 1.78));
        Shape horizontalUpperBlock = new Cube(blockMaterial);
        horizontalUpperBlock.setTransform(ThreeTransform.scaling(11, 15, 0.37).translate(0, -5, -2.04));
        //--
        Shape centralUpperBlock = new Cube(blockMaterial);
        centralUpperBlock.setTransform(ThreeTransform.scaling(11, 10, 0.305).translate(0, -10, 1.57));
        Shape centralLowerBlock = new Cube(blockMaterial);
        centralLowerBlock.setTransform(ThreeTransform.scaling(11, 10, 0.37).translate(0, -10, -2.04));

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
        Group horizontalSteps = new Group(horizontalKeys);
        //--
        Camera cam = centralSetup.makeCamera();
        IVector centralDisp = new Vector(0.5, -1, 0);
        List<Hittable> centralStepList = new ArrayList<>();
        ThreeTransform centralPush = ThreeTransform.translation(-1.81 - centralDisp.x(), -10 - centralDisp.y(), -1 - centralDisp.z());
        for (int i = 0; i < 50; i++) {
            Cube key = new Cube(blockMaterial);
            centralPush = centralPush.translate(centralDisp.x(), centralDisp.y(), centralDisp.z());
            ThreeTransform keyTransform = ThreeTransform.scaling(0.2, 10, 0.58).transform(centralPush);
            key.setTransform(keyTransform);
            IPoint closerCorner = key.getCornerPoint(CubeCorner.FOURTH);
            IPoint fartherCorner = key.getCornerPoint(CubeCorner.FIRST);
            Pixel fartherCornerPixel = cam.projectOnSensorPlane(fartherCorner).get();
            Pixel middleCanvasPixel = new Pixel(fartherCornerPixel.x, size /2.0);
            IPoint desiredFartherLocation = middleCanvasPixel.materialise(cam, new Plane(new Vector(0, 1, 0), fartherCorner));
            double scaledelta = Math.abs(desiredFartherLocation.z() - closerCorner.z())/ Math.abs(fartherCorner.z() - closerCorner.z());
            ThreeTransform deltaTransform = ThreeTransform.scaling(1, 1, scaledelta).conjugateTranslating(closerCorner);
            ThreeTransform newTransform = keyTransform.transform(deltaTransform);
            key.setTransform(newTransform);


            centralStepList.add(key);
        }
        Group centralSteps = new Group(centralStepList);

        //light
        Shape emitent = new Rectangle(emitentMaterial);
        emitent.setTransform(ThreeTransform.rotation_x(Math.PI / 2).scale(5, 1, 5).translate(0, 10, -2));

        //worlds
        World frontWorld = new LambertianWorld(backgroundColour);
        frontWorld.put(emitent).put(horizontalUpperBlock).put(horizontalLowerBlock);
        frontWorld.put(horizontalSteps);
        //
        World centralWorld = new LambertianWorld(backgroundColour);
        centralWorld.put(emitent).put(centralLowerBlock).put(centralUpperBlock);
        centralWorld.put(centralSteps);

        //render
        centralSetup.render(centralWorld);
        centralSetup.export();
    }
}
