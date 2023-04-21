package io.raytracer.mechanics;

import java.io.File;

public interface Loader {
    void populate(World world);

    void load(File file);
}
