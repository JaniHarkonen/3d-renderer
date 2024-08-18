## TO DO

### Priority
- implement alpha maps

### General
- there is still a lot of repetition among render passes, however, this should be further examined once instanced rendering is considered

### Scene
- remove debug-related code
- create a TestScene that possibly extends Scene

### ASceneObject
- when deep copying scene objects for rendering, determine a way to skip unchanged objects

### Window
- create getters and setters for window attributes (such as, dimensions, position, vsync, fullscreen mode, fps limiter, etc.)
- add functionality for re-creating the window (this entails re-generation of OpenGL-assets that can't be transfered between contexts, like VAOs)

### SceneRenderPass
- SceneRenderPass should not be dependent on the CascadeShadowRenderPass-instance, decouple once uniform architecture has been settled

### ShaderProgram
- consider using OpenGL's uniform buffer objects instead of plain uniforms

### VAO
- consider switching back to primitive arrays instead of Vector3f-arrays for lower memory consumption

### Application
- tick rate and frame rate seem to cause issues with reading input
