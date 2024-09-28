## TO DO

### Priority
- properties:
 -- prop expression parsing (add hex color values)
 -- add an isNumeric-flag to properties to avoid instanceof or data type checks
- see if property names, property types can be changed from Strings to enums
- get rid of width percent and height percent, add a new property field 'orientation' for additional info, or find another way of determining orientation from prop name dynamically
- reconsider storing properties as fields in Context due to redundancy
- Context currently holds no reference to a Font, implement font lookup by name
- implement themes
- rename ASTNode to Evaluable as the class now contains functionality for the nodes to be evaluated

### ASceneObject & AGUIElement
- consider if references to the renderer can be removed (don't use method names like 'rendererEquals' or 'rendererCopy')

### ANetworkMessage
- remove resolve(), must be handled separately by the server and the client

### Server-client shared
- contains a lot of code dependent on either server or client, make the package agnostic

### IRenderPass
- why is there a getGameState? if render strategies use it, why not pass it in the execute() call?

### DebugUtils
- get rid of DebugUtils.log(), switch to using Logger

### IRenderStrategy
- all render strategies should be package private

### Server
- server scene architecture (Game) significantly differs from the client's, settle this

### ConnectionHandler
- server side connection handlers should run on separate threads, currently running on the Networker thread

### Scene
- remove debug-related code
- create a TestScene that possibly extends Scene

### GameState
- it is unclear whether delta-checking procures any performance improvement compared to simply copying scene objects every tick
- implement delistSceneObject() & delistGUIElement()

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
- tick rate and frame rate seem to cause issues with reading input, revamp input so that all events are queued

### Later
- implement alpha maps
