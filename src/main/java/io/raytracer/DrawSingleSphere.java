package io.raytracer;

import io.raytracer.drawing.Picture;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.PPMPicture;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.light.IlluminatedPoint;
import io.raytracer.light.IntersectionList;
import io.raytracer.light.LightSourceImpl;
import io.raytracer.drawing.Material;
import io.raytracer.light.Ray;
import io.raytracer.light.RayImpl;
import io.raytracer.drawing.Sphere;
import io.raytracer.drawing.SphereImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DrawSingleSphere {
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
        LightSourceImpl source = new LightSourceImpl(lightColour, lightPosition);

        Picture picture = new PPMPicture(canvasSidePixels, canvasSidePixels);
        Material material = Material.builder()
                .colour(new ColourImpl(1, 0.2, 1))
                .ambient(0.1)
                .diffuse(0.9)
                .specular(0.9)
                .shininess(200)
                .build();
        Sphere sphere = new SphereImpl(material);

        for (int yPixel = 0; yPixel < canvasSidePixels; yPixel++) {
            double yCoordinate = canvasSide - pixelSize * yPixel;
            for (int xPixel = 0; xPixel < canvasSidePixels; xPixel++) {
                double xCoordinate = -canvasSide + pixelSize * xPixel;
                Point canvasPosition = new PointImpl(xCoordinate, yCoordinate, canvasZCoordinate);
                Ray ray = new RayImpl(rayOrigin, canvasPosition.subtract(rayOrigin).normalise());
                IntersectionList intersections = sphere.intersect(ray);

                if (intersections.hit().isPresent()) {
                    IlluminatedPoint illuminated = ray.getIlluminatedPoint(intersections.hit().get());
                    Colour pointColour = source.illuminate(illuminated);
                    picture.write(xPixel, yPixel, pointColour);
                }
            }
        }

        picture.export(writer);
    }
}
