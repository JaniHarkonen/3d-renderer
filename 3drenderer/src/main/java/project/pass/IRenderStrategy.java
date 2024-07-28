package project.pass;

public interface IRenderStrategy<T extends IRenderPass> {

	public void render(T renderPass);
}
