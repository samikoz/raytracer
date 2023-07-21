package io.raytracer.mechanics;

import java.io.File;
import java.io.FileNotFoundException;

public interface Loader {
    void populate(World world);
    void load(File file) throws FileNotFoundException;
}
