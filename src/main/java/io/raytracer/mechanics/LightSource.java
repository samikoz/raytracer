package io.raytracer.mechanics;

import io.raytracer.tools.LinearColour;
import io.raytracer.geometry.IVector;

import io.raytracer.materials.Material;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.raytracer.tools.IColour;
import io.raytracer.geometry.IPoint;

@RequiredArgsConstructor
public class LightSource implements ILightSource {
    @NonNull public final IColour colour;
    @Getter @NonNull private final IPoint position;

    public IColour illuminate(RayHit hitpoint) {
        Material material = hitpoint.object.getMaterial();
        IColour effectiveColour = hitpoint.object.getIntrinsicColour(hitpoint.point).mix(this.colour);
        IVector sourceDirection = this.position.subtract(hitpoint.point).normalise();
        IColour ambientContribution = effectiveColour.multiply(material.ambient);
        double lightNormalCosine = sourceDirection.dot(hitpoint.normalVector);

        IColour diffuseContribution;
        IColour specularContribution;
        if (lightNormalCosine < 0) {
            diffuseContribution = new LinearColour(0, 0, 0);
            specularContribution = new LinearColour(0, 0, 0);
        } else {
            if (hitpoint.shadowed) {
                diffuseContribution = new LinearColour(0, 0, 0);
            } else {
                diffuseContribution = effectiveColour.multiply(material.diffuse).multiply(lightNormalCosine);
            }
            IVector reflectionVector = sourceDirection.negate().reflect(hitpoint.normalVector);
            double reflectionEyeCosine = reflectionVector.dot(hitpoint.eyeVector);

            if (reflectionEyeCosine <= 0 || hitpoint.shadowed) {
                specularContribution = new LinearColour(0, 0, 0);
            } else {
                double specularFactor = Math.pow(reflectionEyeCosine, material.shininess);
                specularContribution = this.colour.multiply(material.specular).multiply(specularFactor);
            }
        }
        return ambientContribution.add(diffuseContribution).add(specularContribution);
    }
}
