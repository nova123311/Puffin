package com.bird.puffin;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	private int VBO, VAO;
	private float[] vertices;

	public Mesh(float[] vertices) {
		this.vertices = vertices;
		setupMesh();
	}
	
	public void render() {
		glBindVertexArray(VAO);
		glDrawArrays(GL_TRIANGLES, 0, 3);
		glBindVertexArray(0);
	}
	
	private void setupMesh() {
		VBO = glGenBuffers();
		VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		glBindVertexArray(0);
	}
}