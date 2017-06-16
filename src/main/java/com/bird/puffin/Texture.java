package com.bird.puffin;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;

public class Texture {

	public Texture(String texturePath) {
		
		// create texture
		int[] width = new int[1];
		int[] height = new int[1];
		int[] channels = new int[1];
		ByteBuffer data = stbi_load(texturePath, width, height, channels, 0);
	}
}