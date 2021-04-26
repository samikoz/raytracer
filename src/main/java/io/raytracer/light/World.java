package io.raytracer.light;

import io.raytracer.drawing.Camera;
import io.raytracer.drawing.Canvas;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.Drawable;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;

public interface World {
    World put(LightSource source);
    World put(Drawable object);
    boolean contains(Drawable object);

    IntersectionList intersect(Ray ray);
    Colour illuminate(Ray ray);

    Transformation getViewTransformation(Point eyePosition, Point lookPosition, Vector upDirection);

    Canvas render(Camera camera);
}
