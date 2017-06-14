package com.bird.puffin;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.IntBuffer;

public class Mesh {
	private int EBO, VBO, VAO;
	private float[] vertices;
	private int[] indices;

	public Mesh(float[] vertices, int[] indices) {
		this.vertices = vertices;
		this.indices = indices;
		setupMesh();
	}
	
	public void render() {
		glBindVertexArray(VAO);
		glDrawElements(GL_TRIANGLES, IntBuffer.wrap(indices));
		glBindVertexArray(0);
	}
	
	private void setupMesh() {
		EBO = glGenBuffers();
		VBO = glGenBuffers();
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		glBindVertexArray(0);
	}
}