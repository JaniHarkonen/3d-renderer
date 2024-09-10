## TO DO

### Later
- implement alpha maps

### ANetworkMessage
- remove resolve(), must be handled separately by the server and the client

### Server-client shared
- contains a lot of code dependent on either server or client, make the package agnostic

### Server
- server scene architecture (Game) significantly differs from the client's, settle this

### ConnectionHandler
- server side connection handlers should run on separate threads, currently running on the Networker thread

### General
- there is still a lot of repetition among render passes, however, this should be further examined once instanced rendering is considered

### Scene
- remove debug-related code
- create a TestScene that possibly extends Scene

### GameState
- it is unclear whether delta-checking procures any performance improvement compared to simply copying scene objects every tick

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
