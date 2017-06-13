package com.bird.puffin;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL.*;

public class Main {

	public static void main(String[] args) {
		
		// create window
		Window window = new Window(800, 600);
		glfwMakeContextCurrent(window.getWindow());
		
		// allow GL bindings to be used
		createCapabilities();
		
		// set GL states
		glViewport(0, 0, 800, 600);
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		
		// create shader
		Shader shader = new Shader("src/main/resources/shader.vs", "src/main/resources/shader.frag");
		
		// triangle vertex data
		float[] vertices = {
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				0.0f, 0.5f, 0.0f
		};
		
		// vertex buffer and vertex array objects
		int VBO = glGenBuffers();
		int VAO = glGenVertexArrays();
		glBindVertexArray(VAO);
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		
		// main game loop
		while (!glfwWindowShouldClose(window.getWindow())) {
			
			// take inputs
			glfwPollEvents();
			processInput(window.getWindow());
			
			// render
			glClear(GL_COLOR_BUFFER_BIT);
			shader.use();
			glBindVertexArray(VAO);
			glDrawArrays(GL_TRIANGLES, 0, 3);
			
			// refresh display
			glfwSwapBuffers(window.getWindow());
		}
		
		// terminate glfw after successful execution
		glfwTerminate();
	}
	
	private static void processInput(long window) {
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window, true);
	}
}