package project.opengl;

import java.util.HashSet;
import java.util.Set;

public class TextureCache {

	private Set<Texture> textureSet;
	
	public TextureCache() {
		this.textureSet = new HashSet<>();
	}
	
	public void generateIfNotEncountered(Texture texture) {
		if( !this.textureSet.contains(texture) ) {
			texture.init();
			this.textureSet.add(texture);
			System.out.println("not encountered");
		}
	}
}
