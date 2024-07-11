## TO DO

### Window
- create getters and setters for window attributes (such as, dimensions, position, vsync, fullscreen mode, fps limiter, etc.)
- add functionality for re-creating the window (this entails re-generation of OpenGL-assets that can't be transfered between contexts, like VAOs)

### Renderer
- separate Renderer into different render passes, for example, color pass for scene rendering, shadow pass for shadows etc.

### ShaderProgram
- create classes for different types of uniforms, however, this should be done later when uniform objects come into play

### Camera
- Camera should utilize the transform provided by the SceneObject instead of having a separate rotation2D-field

### VAO
- mesh info should not be represented by arrays, possibly
- consider creating a VBO-class for VBO generation

### SceneObject
- rename to ASceneObject as the class is abstract

### Input
- cursor should be disabled as resetting the mouse position yields volatile results

### Controller
- create a new Controller-class whose responsibility it is to map input events into actions that control SceneObjects
