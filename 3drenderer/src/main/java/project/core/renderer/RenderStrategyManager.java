package project.core.renderer;

import java.util.HashMap;
import java.util.Map;

import project.core.IRenderable;

public class RenderStrategyManager<P extends IRenderPass, R extends IRenderable> {

	private final Map<Class<?>, IRenderStrategy<P, R>> renderStrategies;
	private final IRenderStrategy<P, R> defaultStrategy;
	
	public RenderStrategyManager(IRenderStrategy<P, R> defaultStrategy) {
		this.renderStrategies = new HashMap<>();
		this.defaultStrategy = defaultStrategy;
	}
	
	
	public RenderStrategyManager<P, R> addStrategy(
		Class<?> clazz, IRenderStrategy<P, R> renderStrategy
	) {
		this.renderStrategies.put(clazz, renderStrategy);
		return this;
	}
	
	public IRenderStrategy<P, R> getStrategy(Class<?> clazz) {
		return this.renderStrategies.getOrDefault(clazz, this.defaultStrategy);
	}
}
