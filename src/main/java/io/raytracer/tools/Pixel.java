package io.raytracer.tools;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Pixel implements Serializable {
    public int x;
    public int y;
}
