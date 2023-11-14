package io.raytracer.demos;

import io.raytracer.geometry.ILine;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
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
import io.raytracer.tools.ClosedPixelCurve;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.Pixel;
import io.raytracer.tools.parsers.LiteralParser;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Monument {
    public static void main(String[] args) throws IOException {
        int xSize = 1080;
        int ySize = 1080;
        double viewAngle = Math.PI / 3.5;
        IColour backgroundColour = new LinearColour(0, 0, 0);

        //setups
        DemoSetup verticalSetup = DemoSetup.builder()
                .rayCount(1000)
                .xSize(xSize)
                .ySize(ySize)
                .viewAngle(viewAngle)
                .upDirection(new Vector(0, 0, 1))
                .eyePosition(new Point(0, 5, 1.55))
                .lookDirection(new Vector(0, -5, -1.55))
                .filename("monument.ppm")
                .bufferDir("./buffStairsVertical/")
                .bufferFileCount(100)
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

        //keys
        Shape upperBlock = new Cube(blockMaterial);
        upperBlock.setTransform(ThreeTransform.scaling(11, 12, 0.3).translate(0, -10, 6.15));
        Shape lowerBlock = new Cube(keyMaterial);
        lowerBlock.setTransform(ThreeTransform.scaling(11, 20, 0.45).translate(0, -10, -2.31));
        verticalObjects.add(upperBlock);
        verticalObjects.add(lowerBlock);

        //stairs
        LiteralParser parser = new LiteralParser();
        parser.parse(new File("monumentCurve.pxs"));
        List<Pixel> pixelList = parser.getParsedPoints().stream().map(point -> new Pixel(point.x(), point.y())).collect(Collectors.toList());
        ClosedPixelCurve pixelCurve = new ClosedPixelCurve(pixelList);

        IVector vertDisp = new Vector(0.6, -1, 0.25);
        List<Hittable> verticalStairs = new ArrayList<>();
        ThreeTransform vertPush = ThreeTransform.scaling(1.8, 10, 0.1).translate(1 - vertDisp.x(), -10 - vertDisp.y(), -1.7 - vertDisp.z());
        Camera camera = verticalSetup.makeCamera();

        IPoint initialPosition = new Cube().getCornerPoint(CubeCorner.FOURTH);
        for (int i = 0; i < 35; i++) {
            vertPush = vertPush.translate(vertDisp.x(), vertDisp.y(), vertDisp.z()).scale(0.8, 1, 1);
            IPoint positionPoint = vertPush.act(initialPosition);
            Plane positionPlane = new Plane(new Vector(0, 1, 0), positionPoint);
            Pixel positionPixel = camera.projectOnSensorPlane(positionPoint).get();
            for (Pair<Integer, Integer> xBounds : pixelCurve.getBoundsForY(positionPixel.y)) {
                Cube block = new Cube(keyMaterial);
                IPoint fourthCornerPosition = positionPlane.intersect((ILine) camera.getRayThrough(new Pixel(xBounds.getValue0(), positionPixel.y))).get();
                IPoint seventhCornerPosition = positionPlane.intersect((ILine) camera.getRayThrough(new Pixel(xBounds.getValue1(), positionPixel.y))).get();
                ITransform cubeTransform = Monument.makePositionTransform(fourthCornerPosition, seventhCornerPosition);
                block.setTransform(cubeTransform);
                verticalStairs.add(block);
            }
        }

        verticalObjects.add(new Group(verticalStairs.toArray(new Hittable[] {})));

        //light
        Shape verticalEmitent = new Rectangle(verticalEmitentMaterial);
        verticalEmitent.setTransform(ThreeTransform.rotation_x(Math.PI / 2 + Math.PI/6).scale(5, 1, 5).translate(0, 10, -2));
        verticalObjects.add(verticalEmitent);

        //worlds
        World verticalWorld = new LambertianWorld(backgroundColour);
        verticalWorld.put(new Group(verticalObjects.toArray(new Hittable[] {})));

        //render
        verticalSetup.render(verticalWorld);
        verticalSetup.export();
    }

    private static ITransform makePositionTransform(IPoint fourthPosition, IPoint seventhPosition) {
        ThreeTransform scale = ThreeTransform.scaling(1, 10, 0.1);
        Cube c = new Cube();
        c.setTransform(scale);
        ITransform xPositionTransform = ThreeTransform
                .translation(-c.getCornerPoint(CubeCorner.FOURTH).x(), -c.getCornerPoint(CubeCorner.FOURTH).y(), -c.getCornerPoint(CubeCorner.FOURTH).z())
                .scale(Math.abs(fourthPosition.x() - seventhPosition.x())/2, 1, 1)
                .translate(fourthPosition.x(), fourthPosition.y(), fourthPosition.z());
        return scale.transform(xPositionTransform);

    }
}
