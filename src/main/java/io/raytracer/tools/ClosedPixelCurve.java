package io.raytracer.tools;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ClosedPixelCurve {
    private final Pixel[] pixelPoints;

    public ClosedPixelCurve(Collection<Pixel> pixels) {
        this.pixelPoints = pixels.toArray(new Pixel[]{});
    }

    public List<Pair<Integer, Integer>> getBoundsForY(int yLevel) {
        List<Integer> bounds = new ArrayList<>();
        List<Pair<Integer, Integer>> boundPairs = new ArrayList<>();
        List<Function<Integer, Boolean>> comparators = new ArrayList<>(Arrays.asList(
            y -> y > yLevel, y -> y < yLevel
        ));
        int comparatorIndex = comparators.get(0).apply(pixelPoints[0].y) ? 0 : 1;
        Function<Integer, Boolean> currentComparator = comparators.get(comparatorIndex);
        for (int i = 0; i < pixelPoints.length - 1; i++) {
            Pixel first = pixelPoints[i];
            Pixel second = pixelPoints[i+1];
            if (!currentComparator.apply(second.y)) {
                double interpolationParameter = (double)(yLevel-first.y)/(second.y-first.y);
                bounds.add(first.interpolate(second, interpolationParameter).x);
                currentComparator = comparators.get(++comparatorIndex % 2);
            }
        }
        assert bounds.size() % 2 == 0;
        bounds.sort(Comparator.comparingInt(x -> -x));
        for (int i = 0; i < bounds.size(); i+=2) {
            boundPairs.add(new Pair<>(bounds.get(i), bounds.get(i+1)));
        }
        return boundPairs;
    }
}
