# Rough ideas
- Self-containment, stacking like stairs
- Merging perspectives like Cézanne or eyesight
- Windows

# Technical Resources
1. https://raytracing.github.io/

# Dev
- Should avoid inverting matrices as often as possible. e.g. for scenes with lots of spheres could implement
simple sphere which for scaling and translation just changes radius and centre and implements general
ray-intersection solver for these.
- Think about the current recursive implementation of illuminate. Can't we pass the "colour so far"
parameter and cut off the subsequent invocations if their contribution would be too small to notice
- Groups have been implemented for grouping but not for bounding boxes
- For textures, look whether can group Mapping parameter together with IPoint in one class. 
Could then pass this class to texture.colourAt and also implement matrix-transformations for it.