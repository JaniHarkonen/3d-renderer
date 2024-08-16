## TO DO

### Priority
- implement alpha maps
- get rid of RendererGL casts inside 'opengl' package

### General
- there is still a lot of repetition among render passes, however, this should be further examined once instanced rendering is considered
- get rid of uniform casting in render strategies

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

### Input
- add NoKeyAction, AnyKey, NoMouseAction input events

### AInputEvent
- perhaps input events should be static classes of Input instead of being tied to an Input instance by default

### Application
- tick rate and frame rate seem to cause issues with reading input
