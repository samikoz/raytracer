package io.raytracer.tools;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PixelCurveTest {

    @Test
    void getBoundsForY() {
        List<Pixel> curvePoints = Arrays.asList(
                new Pixel(10, 100), new Pixel(10, 10), new Pixel(70, 70), new Pixel(100,30), new Pixel(100, 100), new Pixel(10, 100)
        );
        PixelCurve curve = new PixelCurve(curvePoints);
        Set<Pair<Integer, Integer>> expectedPairs = new HashSet<>(Arrays.asList(new Pair<>(50, 10), new Pair<>(100, 85)));

        Set<Pair<Integer, Integer>> pixelPairs = new HashSet<>(curve.getXCoordsForY(50));

        assertEquals(expectedPairs, pixelPairs);
    }

    @Test
    void getBoundsForLessConvexCurve() {
        List<Pixel> curvePoints = Arrays.asList(
          new Pixel(100, 100), new Pixel(100, 0), new Pixel(40, 60), new Pixel(0,0), new Pixel(10, 100), new Pixel(70, 40), new Pixel(100, 100)
        );
        PixelCurve curve = new PixelCurve(curvePoints);
        Set<Pair<Integer, Integer>> expectedPairs = new HashSet<>(Arrays.asList(new Pair<>(100, 75), new Pair<>(60, 50), new Pair<>(200/6, 5)));

        Set<Pair<Integer, Integer>> pixelPairs = new HashSet<>(curve.getXCoordsForY(50));

        assertEquals(expectedPairs, pixelPairs);
    }
}