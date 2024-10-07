## TO DO

### Priority
- add responsiveness queries to Jeemu
- reconsider storing properties as fields in Context due to redundancy
- Context currently holds no reference to a Font, implement font lookup by name
- implement a markup language for building UIs with
 -- implement Jeemu parser that will generate 
- expr parenthesis checking is currently broken
- StyleCascade in 'opengl.gui' shouldn't have to evaluate the props, this should be done by the GUI
 -- Property should propagate changes to Properties which should propagate changes to AGUIElement which should cascade changes to its children
- rename GUI to UI across the code base
- AGUIElement's children-field should be private as all changes to children should be done through the GUI in order to keep the element table consistent

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
