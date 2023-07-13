# Rough ideas
- Mirrors, rotating mirrors, then rotating objects
- Self-containment, stacking like stairs
- Merging perspectives like Cézanne or eyesight
- Windows

# Technical Resources
1. https://raytracing.github.io/

# Technicalities
- Concoct "Recasters" static class with FucntionalInterface fields for Material.recaster(recaster, contribution)
returning Collection of recast rays. This way can reconcile Lambertian and Phong worlds.
- Think about the current recursive implementation of illuminate. Can't we pass the "colour so far"
parameter and cut off the subsequent invocations if their contribution would be too small to notice?
- Intersection for cube can be optimised. To be found somewhere in TR1., "the next week".
- Groups have been implemented for grouping but not for bounding boxes
- Really the current Tuple-based implementation is too general. Should be having
classes assuming 3 dimensions.