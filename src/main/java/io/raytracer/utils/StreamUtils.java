package io.raytracer.utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toCollection;

public class StreamUtils {
    static public <T> Collector<T, ?, Stream<T>> shuffledCollector() {
        return Collectors.collectingAndThen(
                toCollection(ArrayList::new),
                list -> !list.isEmpty()
                        ? StreamSupport.stream(new RandomSpliterator<>(list, Random::new), false)
                        : Stream.empty());
    }
}
