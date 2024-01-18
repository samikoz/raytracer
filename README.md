# Rough ideas
- Self-containment, stacking like stairs
- Merging perspectives like Cézanne or eyesight
- Windows

# Technical Resources
1. https://raytracing.github.io/

# Dev
- Revisit and make right the matrices if you feel like.
- Could avoid inverting matrices as often as possible. e.g. for scenes with lots of spheres could implement
simple sphere which for scaling and translation just changes radius and centre and implements general
ray-intersection solver for these.
- No tests for reflection, refraction etc. Have messed with refraction, so before you use
it make sure it works properly.
- For texture parameters add separate methods to interface. Write separate classes so that can switch between them.
Spring, finally? look whether can group Mapping parameter together with IPoint in one class[main](src%2Fmain)