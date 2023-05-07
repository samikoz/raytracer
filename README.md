# Rough ideas
- Mirrors, rotating mirrors, then rotating objects
- Self-containment, stacking like stairs
- Merging perspectives like Cézanne or eyesight
- Windows

# Technical Resources
1. https://raytracing.github.io/

# Technicalities
- Intersection for cube can be optimised. To be found somewhere in TR1., "the next week".
- Antialiasing can be added, some preparations have been made by World.illuminate accepting
a collection of rays. At the moment it only selects the first one.
- Groups have been implemented for grouping but not for bounding boxes
- Really the current Tuple-based implementation is too general. Should be having
classes assuming 3 dimensions.
- Look at TR1, "weekend" for implementation of matte surfaces. Will want to implement it
aside out Phong reflection model to be able to choose and compare.