package io.raytracer.demos;

import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.World;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Hittable;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import lombok.Getter;

import java.io.IOException;

@Getter
public abstract class Painter {
    protected DemoSetup setup;
    public Painter(DemoSetup setup) {
        this.setup = setup;
    }

    abstract protected void setSetup();

    protected LinearColour backgroundColour() {
        return new LinearColour(0);
    }

    abstract protected Hittable[] makeObjects();

    public IPicture render() throws IOException {
        this.setSetup();

        Group objects = new Group(this.makeObjects());
        World world = new LambertianWorld(this.backgroundColour());
        world.put(objects);

        IPicture picture = this.setup.makePicture();
        world.render(picture, this.setup.makeCamera());
        return picture;
    }
}
