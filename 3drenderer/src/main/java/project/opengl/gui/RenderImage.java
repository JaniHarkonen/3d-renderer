package project.opengl.gui;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import project.component.Transform;
import project.core.renderer.IRenderStrategy;
import project.core.renderer.IRenderer;
import project.gui.Image;
import project.opengl.TextureGL;
import project.opengl.vao.VAO;
import project.scene.ASceneObject;

public class RenderImage implements IRenderStrategy<GUIRenderPass> {

	@Override
	public void execute(IRenderer renderer, GUIRenderPass renderPass, ASceneObject target) {
		Image element = (Image) target;
		
		TextureGL textureGL = (TextureGL) element.getTexture().getGraphics();
		Vector4f primaryColor = element.getPrimaryColor();
		
		renderPass.uPrimaryColor.update(primaryColor);
		GL46.glActiveTexture(GL46.GL_TEXTURE0);
		textureGL.bind();
		
		Transform transform = element.getTransform();
		Vector3f position = transform.getPosition();
		Vector2f anchor = element.getAnchor();
		Quaternionf rotation = transform.getRotator().getAsQuaternion();
		
		renderPass.uObjectTransform.update(
			new Matrix4f()
			.translationRotateScale(
				position.x - anchor.x, position.y - anchor.y, 0.0f,
				rotation.x, rotation.y, rotation.z, rotation.w,
				1.0f, 1.0f, 1.0f
			)
		);
		
		VAO vao = (VAO) renderPass.imagePlane.getGraphics();
		vao.bind();
		GL46.glDrawElements(GL46.GL_TRIANGLES, vao.getVertexCount() * 3, GL46.GL_UNSIGNED_INT, 0);
	}
}
