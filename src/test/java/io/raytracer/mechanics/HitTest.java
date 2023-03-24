package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.drawables.Drawable;
import io.raytracer.drawables.Sphere;
import io.raytracer.materials.Glass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HitTest {
    static IRay testRay;

    @BeforeAll
    static void setupMaterialAndPosition() {
        testRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
    }

    @Test
    void hitWithPositiveIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,0.1, new Sphere());
        Optional<Hit> expectedHit = Hit.fromIntersections(new Intersection[] { i1, i2 });

        assertTrue(expectedHit.isPresent());
        assertEquals(0.1, expectedHit.get().rayParameter, 1e-3,
                "Hit should be with lowest positive parameter value");
    }

    @Test
    void hitWithSomeNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        Optional<Hit> expectedHit = Hit.fromIntersections(new Intersection[] { i1, i2 });

        assertTrue(expectedHit.isPresent());
        assertEquals(2.0, expectedHit.get().rayParameter, 1e-3,
                "Hit should be with the lowest positive parameter value.");
    }

    @Test
    void hitWithAllNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,-2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        Optional<Hit> expectedHit = Hit.fromIntersections(new Intersection[] { i1, i2 });

        assertFalse(expectedHit.isPresent(), "All negative intersections should produce no hit.");
    }

    @Test
    void getMaterialPoint() {
        Ray ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection intersection = new Intersection(ray,4, sphere);
        Optional<Hit> hit = Hit.fromIntersections(new Intersection[] { intersection });

        assertTrue(hit.isPresent());
        MaterialPoint illuminated = hit.get().getMaterialPoint();

        assertEquals(sphere, illuminated.object);
        assertEquals(new Point(0, 0, -1), illuminated.point);
        assertEquals(new Vector(0, 0, -1), illuminated.eyeVector);
        assertEquals(new Vector(0, 0, -1), illuminated.normalVector);
    }

    @Test
    void getMaterialPointFromInside() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection intersection = new Intersection(ray,1, sphere);
        Optional<Hit> hit = Hit.fromIntersections(new Intersection[] { intersection });

        assertTrue(hit.isPresent());
        MaterialPoint illuminated = hit.get().getMaterialPoint();

        assertEquals(new Point(0, 0, 1), illuminated.point);
        assertEquals(new Vector(0, 0, -1), illuminated.eyeVector);
        assertEquals(new Vector(0, 0, -1), illuminated.normalVector,
                "Normal vector should be inverted");
    }

    private static Stream<Arguments> provideIntersectionIndicesAndRefractionIndices() {
        return Stream.of(
                Arguments.of(new Point(0, 0, -2.5), 1.0, 1.5),
                Arguments.of(new Point(0, 0, -1.5), 1.5, 2.0),
                Arguments.of(new Point(0, 0, -1), 2.0, 2.5),
                Arguments.of(new Point(0, 0, 0), 2.5, 2.5),
                Arguments.of(new Point(0, 0, 1), 2.5, 1.5),
                Arguments.of(new Point(0, 0, 1.5), 1.5, 1.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideIntersectionIndicesAndRefractionIndices")
    void refractiveIndices(Point rayStartPoint, double refractiveFrom, double refractiveTo) {
        Drawable outerSphere = new Sphere(Glass.glassBuilder().refractiveIndex(1.5).build());
        outerSphere.setTransform(ThreeTransform.scaling(2, 2, 2));
        Drawable leftSphere = new Sphere(Glass.glassBuilder().refractiveIndex(2.0).build());
        leftSphere.setTransform(ThreeTransform.translation(0, 0, -0.25));
        Drawable rightSphere = new Sphere(Glass.glassBuilder().refractiveIndex(2.5).build());
        rightSphere.setTransform(ThreeTransform.translation(0, 0, 0.25));
        World world = (World)new World().put(outerSphere).put(leftSphere).put(rightSphere);

        IRay ray = new Ray(rayStartPoint, new Vector(0, 0, 1));
        Intersection[] intersections = world.intersect(ray);
        Optional<Hit> hit = Hit.fromIntersections(intersections);
        assertTrue(hit.isPresent());
        MaterialPoint point = hit.get().getMaterialPoint();

        assertEquals(refractiveFrom, point.refractiveIndexFrom);
        assertEquals(refractiveTo, point.refractiveIndexTo);
    }
}