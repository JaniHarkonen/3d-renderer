# 3D renderer

This project will function as a simple testing ground for an OpenGL-based renderer written in Java. The ultimate goal is to have a renderer with at least the following features:
- support for diffuse, normal maps and roughness maps
- support for ambient, directional, point and spotlights
- cascaded shadow maps
- ability to import models from external files
- support for skeletal animations

The application will be single-threaded, and uses OpenGL-bindings provided by the LWJGL-library. The JOML-library is used for 3D-math related stuff (such as vectors, matrices,
interesections etc).
