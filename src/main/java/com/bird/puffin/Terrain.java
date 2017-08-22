package com.bird.puffin;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.IntBuffer;

public class Terrain {
	private int EBO, VBO, VAO;
	private float vertices[];
	private int indices[];
	private final int SIZE = 1024;
	
	public Terrain() {
		vertices = new float[3 * (SIZE + 1) * (SIZE + 1)];
		indices = new int[6 * SIZE * SIZE];
		
		// compute vertex positions
		int currentIndex = 0;
		for (int z = 0; z <= SIZE; ++z) {
			for (int x = 0; x <= SIZE; ++x) {
				float y = 0;
				vertices[currentIndex++] = x;
				vertices[currentIndex++] = y;
				vertices[currentIndex++] = z;
			}
		}
		
		// compute indices
		currentIndex = 0;
		for (int x = 0; x < SIZE; ++x) {
			for (int y = 0; y < SIZE; ++y) {
				
				// 1st part of tile (upper left triangle)
				indices[currentIndex++] = x * (SIZE + 1) + y;
				indices[currentIndex++] = x * (SIZE + 1) + y + 1;
				indices[currentIndex++] = x * (SIZE + 1) + y + SIZE + 1;
				
				// 2nd part of tile (bottom right triangle)
				indices[currentIndex++] = x * (SIZE + 1) + y + 1;
				indices[currentIndex++] = x * (SIZE + 1) + y + SIZE + 1;
				indices[currentIndex++] = x * (SIZE + 1) + y + SIZE + 2;
			}
		}
		
		// create mesh to draw
		setupMesh();
	}
	
	public void render(Shader shader) {
		shader.use();
		glBindVertexArray(VAO);
		glDrawElements(GL_LINE_STRIP, IntBuffer.wrap(indices));
		glBindVertexArray(0);
	}
	
	private void setupMesh() {
		
		// generate buffers
		EBO = glGenBuffers();
		VBO = glGenBuffers();
		VAO = glGenVertexArrays();
		
		// bind VBO and EBO to VAO
		glBindVertexArray(VAO);
		
		// bind vertex data
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		// bind index data
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		// vertex position
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 12, 0);
		glEnableVertexAttribArray(0);
		
		// unbind the VAO
		glBindVertexArray(0);
	}
}
