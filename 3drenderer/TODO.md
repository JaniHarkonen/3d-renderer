## TO DO

### Priority
- implement roughness maps, alpha maps
- get rid of RendererGL casts inside 'opengl' package
- consider AAsset
- resolve AGUIElement vs ASceneObject conflict
- ensure consistent code style in shaders
- re-think animation & bone architecture

### Formatting
- switch from using ClassName.STATIC_FINAL_FIELD or ClassName.STATIC_METHOD to simply STATIC_FINAL_FIELD or STATIC_METHOD to make refactoring easier
- use initialize() instead of init() for initialization methods

### General
- restrict visibilities of class to the lowest level needed
- avoid using static blocks
- there is still a lot of repetition among render passes, however, this should be further examined once instanced rendering is considered
- get rid of uniform casting in render strategies

### ASceneObject
- when deep copying scene objects for rendering, determine a way to skip unchanged objects
- when deep copying scene objects for rendering, examine the degree to which fields need to be copied (for example, copying animations can be VERY costly)

### AGUIElement & ASceneObject
- AGUIElement and ASceneObject are essentially the same, combine the two or create a new class

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

### Font
- consider using an array instead of a map for quicker glyph access by character

### Text
- Font and color probably shouldn't be a part of Text (maybe set a draw font for draw calls), consider this when implementing GUI further

### SceneAssetLoadTask
- re-think animation & bone architecture

### DepthTexture
- should this be called something other than texture as it is essentially an array of textures
