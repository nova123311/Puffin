package com.bird.puffin;

import java.nio.IntBuffer;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	private int EBO, VBO, VAO;
	private float[] vertices;
	private int[] indices;
	private Material material;

	public Mesh(float[] vertices, int[] indices, Material material) {
		this.vertices = vertices;
		this.indices = indices;
		this.material = material;
		setupMesh();
	}
	
	public void render(Shader shader) {
		shader.use();
		Vector3f ambient = material.getAmbient();
		Vector3f diffuse = material.getDiffuse();
		Vector3f specular = material.getSpecular();
		float shininess = material.getShininess();
		shader.setFloat("material.ambient", ambient.x, ambient.y, ambient.z);
		shader.setFloat("material.diffuse", diffuse.x, diffuse.y, diffuse.z);
		shader.setFloat("material.specular", specular.x, specular.y, specular.z);
		shader.setFloat("material.shininess", shininess);
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
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 32, 12);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 32, 24);
		glEnableVertexAttribArray(2);
		glBindVertexArray(0);
	}
}