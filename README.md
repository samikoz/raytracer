# Rough ideas
- Mirrors, rotating mirrors, then rotating objects
- Self-containment, stacking like stairs
- Merging perspectives like Cézanne or eyesight
- Windows

# Technical Resources
1. https://raytracing.github.io/

# Technicalities
- Think about the current recursive implementation of illuminate. Can't we pass the "colour so far"
parameter and cut off the subsequent invocations if their contribution would be too small to notice
- Groups have been implemented for grouping but not for bounding boxes