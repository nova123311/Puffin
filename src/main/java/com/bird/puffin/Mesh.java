package com.bird.puffin;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	private int EBO, VBO, VAO;
	private int[] indices;
	private Material material;
	private Texture diffuseTexture;
	private Texture specularTexture;

	public Mesh(Vertex[] vertices, int[] indices, Material material, Texture diffuseTexture, Texture specularTexture) {
		this.indices = indices;
		this.material = material;
		this.diffuseTexture = diffuseTexture;
		this.specularTexture = specularTexture;
		
		// generate EBO, VBO, and VAO for indexed drawing
		EBO = glGenBuffers();
		VBO = glGenBuffers();
		VAO = glGenVertexArrays();
		
		// bind VBO and EBO to VAO
		glBindVertexArray(VAO);
		
		// bind vertex data
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, genVertices(vertices), GL_STATIC_DRAW);
		
		// bind index data
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		// vertex position
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
		glEnableVertexAttribArray(0);
		
		// vertex normal
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 32, 12);
		glEnableVertexAttribArray(1);
		
		// vertex texture coordinate
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 32, 24);
		glEnableVertexAttribArray(2);
		
		// unbind the VAO
		glBindVertexArray(0);
	}
	
	public void render(Shader shader) {
		shader.use();
		
		// set diffuse textures
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, diffuseTexture.getTexture()[0]);
		shader.setInt("material.diffuse", 0);
		
		// set specular textures
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, specularTexture.getTexture()[0]);
		shader.setInt("material.specular", 1);
		
		// set shininess
		shader.setFloat("material.shininess", material.getShininess());;
		
		// draw the mesh
		glBindVertexArray(VAO);
		glDrawElements(GL_TRIANGLES, IntBuffer.wrap(indices));
		glBindVertexArray(0);
	}
	
	private float[] genVertices(Vertex[] vertices) {
		float[] result = new float[vertices.length * 8];
		for (int i = 0; i < vertices.length; ++i) {
			float[] vertex = vertices[i].getVertex();
			for (int j = 0; j < 8; ++j) {
				result[i * 8 + j] = vertex[j];
			}
		}
		return result;
	}
}