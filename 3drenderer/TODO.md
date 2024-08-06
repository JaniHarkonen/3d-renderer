## TO DO

### Priority
- improve architecture of uniforms
- implement roughness maps, alpha maps
- integrate GameStates into the rendering process
- get rid of RendererGL casts inside 'opengl' package

### Formatting
- switch from using ClassName.STATIC_FINAL_FIELD or ClassName.STATIC_METHOD to simply STATIC_FINAL_FIELD or STATIC_METHOD to make refactoring easier

### General
- restrict visibilities of class to the lowest level needed
- avoid using static blocks
- there is still a lot of repetition among render passes, however, this should be further examined once instanced rendering is considered

### ASceneObject
- when deep copying scene objects for rendering, determine a way to skip unchanged objects
- when deep copying scene objects for rendering, examine the degree to which fields need to be copied (for example, copying animations can be VERY costly)

### AGUIElement & ASceneObject
- AGUIElement and ASceneObject are essentially the same, combine the two or create a new class

### Window
- create getters and setters for window attributes (such as, dimensions, position, vsync, fullscreen mode, fps limiter, etc.)
- add functionality for re-creating the window (this entails re-generation of OpenGL-assets that can't be transfered between contexts, like VAOs)

### Renderer
- separate Renderer into different render passes, for example, color pass for scene rendering, shadow pass for shadows etc.
- graphics assets (VAOs, textures) should be generated before rendering
- Renderer should not reference a Scene all necessary information should be provided by the GameState

### SceneRenderPass
- SceneRenderPass should not be dependent on the CascadeShadowRenderPass-instance, decouple once uniform architecture has been settled

### ShaderProgram
- create classes for different types of uniforms, however, this should be done later when uniform objects come into play
- consider using OpenGL's uniform buffer objects instead of plain uniforms

### VAO
- consider switching back to primitive arrays instead of Vector3f-arrays for lower memory consumption
- consider creating a VBO-class for VBO generation

### Controller
- create a new Controller-class whose responsibility it is to map input events into actions that control SceneObjects

### Input
- add NoKeyAction, AnyKey, NoMouseAction input events

### AInputEvent
- perhaps input events should be static classes of Input instead of being tied to an Input instance by default

### Application
- tick rate and frame rate seem to cause issues with reading input

### Scene & GUI
- GUI should not be a member of Scene, there should be a third class that holds both the Scene and the GUI

### Font
- consider using an array instead of a map for quicker glyph access by character

### Text
- Font and color probably shouldn't be a part of Text (maybe set a draw font for draw calls), consider this when implementing GUI further

### SceneAssetLoadTask
- re-think animation & bone architecture

### DepthTexture
- should this be called something other than texture as it is essentially an array of textures
- see if this class can be somehow merged with Texture given that both have overlap in their implementation
