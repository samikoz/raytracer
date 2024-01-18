package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
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
        @NonNull String name_appendix = args[0];
        IColour backgroundColour = new LinearColour(0, 0, 0);

        //setups
        DemoSetup centralSetup = DemoSetup.builder()
                .rayCount(500)
                .xSize(size)
                .ySize(size)
                .viewAngle(Math.PI / 4)
                .upDirection(new Vector(0, 0, 1))
                .eyePosition(new Point(0, 5, -0.2))
                .lookDirection(new Vector(0, -5, 0))
                .filename(String.format("outputs/trash/centralMod_%s.ppm", name_appendix))
                .bufferDir(String.format("./buffs/refcstairsmod/buffmod_%s/", name_appendix))
                .bufferFileCount(2)
                .build();

        //materials
        Material blockMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.65)))
                .build();

        //blocks
        Shape centralUpperBlock = new Cube(blockMaterial);
        centralUpperBlock.setTransform(ThreeTransform.scaling(11, 10, 0.305).translate(0, -10, 1.57));
        Shape centralLowerBlock = new Cube(blockMaterial);
        centralLowerBlock.setTransform(ThreeTransform.scaling(11, 10, 0.37).translate(0, -10, -2.04));

        //stairs
        double leftStairXMinPosition = -1.82;
        double xScale = 0.25;
        Camera cam = centralSetup.makeCamera();
        IVector centralDisp = new Vector(2*xScale, -1.5, 0);
        List<Hittable> centralStepList = new ArrayList<>();
        ThreeTransform centralPush = ThreeTransform.translation(leftStairXMinPosition - centralDisp.x(), -10 - centralDisp.y(), -1.1 - centralDisp.z());

        for (int i = 0; i < 16; i++) {
            Material currentBlockMaterial = Material.builder()
                    .texture(new MonocolourTexture(new LinearColour(0.6 - Math.max((i - 2)*0.05, 0))))
                    .build();
            Cube key = new Cube(currentBlockMaterial);
            centralPush = centralPush.translate(centralDisp.x(), centralDisp.y(), centralDisp.z());
            ThreeTransform keyTransform = ThreeTransform.scaling(xScale, 10, 0.58).transform(centralPush);
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
        int lightBrightness = 10;
        Material emitentMaterial = Material.builder().emit(new LinearColour(lightBrightness)).build();
        Shape emitent = new Rectangle(emitentMaterial);
        emitent.setTransform(ThreeTransform.rotation_x(Math.PI / 2).scale(5, 1, 5).translate(0, 10, -2));

        //worlds
        World centralWorld = new LambertianWorld(backgroundColour);
        centralWorld.put(emitent);
        centralWorld.put(centralLowerBlock).put(centralUpperBlock);
        centralWorld.put(centralSteps);

        //render
        centralSetup.render(centralWorld);
        centralSetup.export();
    }
}
