package project.opengl;

import java.util.HashMap;
import java.util.Map;

import project.core.renderer.IRenderPass;
import project.core.renderer.IRenderStrategy;

public class RenderStrategyManager<T extends IRenderPass> {

	private final Map<Class<?>, IRenderStrategy<T>> renderStrategies;
	private final IRenderStrategy<T> defaultStrategy;
	
	public RenderStrategyManager(IRenderStrategy<T> defaultStrategy) {
		this.renderStrategies = new HashMap<>();
		this.defaultStrategy = defaultStrategy;
	}
	
	
	public RenderStrategyManager<T> addStrategy(Class<?> clazz, IRenderStrategy<T> renderStrategy) {
		this.renderStrategies.put(clazz, renderStrategy);
		return this;
	}
	
	public IRenderStrategy<T> getStrategy(Class<?> clazz) {
		return this.renderStrategies.getOrDefault(clazz, this.defaultStrategy);
	}
}
