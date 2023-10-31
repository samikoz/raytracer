# Rough ideas
- Self-containment, stacking like stairs
- Merging perspectives like Cézanne or eyesight
- Windows

# Technical Resources
1. https://raytracing.github.io/

# Dev
- Before looking at below - now Cylinder intersections are the bottleneck. Try to make faster.
- Revisit and make right the matrices if you feel like that.
- Could inverting matrices as often as possible. e.g. for scenes with lots of spheres could implement
simple sphere which for scaling and translation just changes radius and centre and implements general
ray-intersection solver for these.
- For textures, look whether can group Mapping parameter together with IPoint in one class. 
Could then pass this class to texture.colourAt and also implement matrix-transformations for it.
- Can implement simplified form of finding refractive indices. Allow for specification which refindex algorithm
one wants. Probably that would lead to some RayHitFactory implementation.
- No tests for reflection, refraction etc. Have messed with refraction, so before you use
it make sure it works properly.