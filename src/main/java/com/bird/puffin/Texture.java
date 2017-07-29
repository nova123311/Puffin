package com.bird.puffin;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
	private int[] texture;
	private int current;

	/**
	 * Create a new texture from specified path(s)
	 * @param paths to texture files to load into the texture
	 */
	public Texture(String... paths) {
		texture = new int[16];
		for (String path : paths)
			load(path);
	}
	
	/**
	 * Use textures for rendering
	 */
	public void use() {
		for (int i = 0; i < current; ++i) {
			glActiveTexture(GL_TEXTURE0 + i);
			glBindTexture(GL_TEXTURE_2D, texture[i]);
		}
	}
	
	/**
	 * Get all the texture ids associated with this texture
	 * @return array of generated texture ids
	 */
	public int[] getTexture() {
		return texture;
	}
	
	/**
	 * Load a texture from a path file and return success of the operation.
	 * An error message is included if loading fails. 
	 * @param path to the texture file to load
	 * @return if the texture was properly loaded
	 */
	public boolean load(String path) {
		int[] width = new int[1];
		int[] height = new int[1];
		int[] channels = new int[1];
		stbi_set_flip_vertically_on_load(true);
		ByteBuffer data = stbi_load(path, width, height, channels, 0);
		
		// file was not loaded properly 
		if (data == null) {
			System.err.println(path + ": " + stbi_failure_reason());
			return false;
		}
		
		// generate and bind opengl texture
		texture[current] = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture[current++]);
		int format = channels[0] == 3 ? GL_RGB : GL_RGBA;
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0], 0, format, GL_UNSIGNED_BYTE, data);
		glGenerateMipmap(GL_TEXTURE_2D);
		return true;
	}
}