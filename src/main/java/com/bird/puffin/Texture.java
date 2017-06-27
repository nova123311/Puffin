package com.bird.puffin;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
	private int[] texture;
	private int current;

	public Texture(String... texturePaths) {
		texture = new int[16];
		current = 0;
		for (String path : texturePaths)
			load(path);
	}
	
	public void use() {
		for (int i = 0; i < current; ++i) {
			glActiveTexture(GL_TEXTURE0 + i);
			glBindTexture(GL_TEXTURE_2D, texture[i]);
		}
	}
	
	public void load(String path) {
		int[] width = new int[1];
		int[] height = new int[1];
		int[] channels = new int[1];
		stbi_set_flip_vertically_on_load(true);
		ByteBuffer data = stbi_load(path, width, height, channels, 0);
		texture[current] = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture[current++]);
		if (channels[0] == 3)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0], 0, GL_RGB, GL_UNSIGNED_BYTE, data);
		if (channels[0] == 4)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		glGenerateMipmap(GL_TEXTURE_2D);
	}
}