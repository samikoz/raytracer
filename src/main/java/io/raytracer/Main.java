package io.raytracer;

import io.raytracer.drawing.Canvas;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.PPMCanvas;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.Vector;
import io.raytracer.light.IlluminatedPoint;
import io.raytracer.light.IntersectionList;
import io.raytracer.light.LightSource;
import io.raytracer.light.Lighting;
import io.raytracer.drawing.Material;
import io.raytracer.light.Ray;
import io.raytracer.light.RayImpl;
import io.raytracer.drawing.Sphere;
import io.raytracer.drawing.SphereImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws IOException {
        String filename = "sphere.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));

        double canvasZCoordinate = 10;
        double canvasSide = 3.5;
        int canvasSidePixels = 300;
        double pixelSize = 2 * canvasSide / canvasSidePixels;

        Point rayOrigin = new PointImpl(0, 0, -5);
        Point lightPosition = new PointImpl(-10, 10, -10);
        Colour lightColour = new ColourImpl(1, 1, 1);
        LightSource source = new LightSource(lightColour, lightPosition);

        Canvas canvas = new PPMCanvas(canvasSidePixels, canvasSidePixels);
        Material material = new Material();
        material.colour = new ColourImpl(1, 0.2, 1);
        Sphere sphere = new SphereImpl(material);

        for (int yPixel = 0; yPixel < canvasSidePixels; yPixel++) {
            double yCoordinate = canvasSide - pixelSize * yPixel;
            for (int xPixel = 0; xPixel < canvasSidePixels; xPixel++) {
                double xCoordinate = -canvasSide + pixelSize * xPixel;
                Point canvasPosition = new PointImpl(xCoordinate, yCoordinate, canvasZCoordinate);
                Ray ray = new RayImpl(rayOrigin, canvasPosition.subtract(rayOrigin).normalise());
                IntersectionList intersections = sphere.intersect(ray);

                if (intersections.hit().isPresent()) {
                    Point hitPoint = ray.position(intersections.hit().get().time);
                    Vector normal = sphere.normal(hitPoint);
                    Vector eyeVector = ray.getDirection().negate();
                    IlluminatedPoint illuminated = new IlluminatedPoint(
                            hitPoint, normal, sphere.getMaterial());
                    Colour pointColour = Lighting.illuminate(source, eyeVector, illuminated);
                    canvas.write(xPixel, yPixel, pointColour);
                }
            }
        }

        canvas.export(writer);
    }
}
