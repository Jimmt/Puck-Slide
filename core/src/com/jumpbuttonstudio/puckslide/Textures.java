package com.jumpbuttonstudio.puckslide;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Textures {
	static HashMap<String, Texture> cache = new HashMap<String, Texture>();

	public static Texture getTex(String path) {
		
		if (cache.containsKey(path)) {
			return cache.get(path);
		} else {
			Texture tex = new Texture(Gdx.files.internal(path));
			tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			cache.put(path, tex);
			return tex;
		}
	}
}
