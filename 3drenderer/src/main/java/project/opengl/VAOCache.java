package project.opengl;

import java.util.HashMap;
import java.util.Map;

import project.asset.Mesh;

public class VAOCache {

	private Map<Mesh, VAO> meshVAOMap;
	
	public VAOCache() {
		this.meshVAOMap = new HashMap<>();
	}
	
	public VAO getOrGenerate(Mesh mesh) {
		VAO vao = this.meshVAOMap.get(mesh);
		
		if( vao == null ) {
			vao = new VAO(mesh);
			vao.init();
			this.meshVAOMap.put(mesh, vao);
		}
		
		return vao;
	}
}
