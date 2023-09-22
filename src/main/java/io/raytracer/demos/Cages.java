package io.raytracer.demos;

import java.io.IOException;

public class Cages {
    public static void main(String[] args) throws IOException {
        int[] rayCounts = new int[] { 100, 200, 400, 800 };
        for (int rayCount : rayCounts) {
            Cage.main(new String[] { String.valueOf(rayCount) });
        }
    }
}
