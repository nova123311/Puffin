package com.bird.puffin;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
	private int texture;

	public Texture(String texturePath) {
		
		// create texture
		int[] width = new int[1];
		int[] height = new int[1];
		int[] channels = new int[1];
		ByteBuffer data = stbi_load(texturePath, width, height, channels, 0);
		
		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0], 0, GL_RGB, GL_UNSIGNED_BYTE, data);
		glGenerateMipmap(GL_TEXTURE_2D);
	}
	
	public void use() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
}