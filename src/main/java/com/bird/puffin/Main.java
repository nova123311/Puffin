package com.bird.puffin;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.*;

public class Main {

	public static void main(String[] args) {
		
		// create window
		Window window = new Window(800, 600);
		glfwMakeContextCurrent(window.getWindow());
		
		// GL stuff 
		createCapabilities();
		glViewport(0, 0, 800, 600);
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		
		// create shader
		Shader shader = new Shader("src/main/resources/shader.vs", "src/main/resources/shader.frag");
		
		// triangle vertex data
		float[] vertices = {
				-0.5f, -0.5f, 0.0f,
				-0.5f, 0.5f, 0.0f,
				0.5f, 0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
		};
		int[] indices = {
				0, 1, 2,
				0, 2, 3
		};
		
		// create mesh to draw
		Mesh mesh = new Mesh(vertices, indices);
		
		// main game loop
		while (!glfwWindowShouldClose(window.getWindow())) {
			
			// take inputs
			glfwPollEvents();
			processInput(window.getWindow());
			
			// shader attributes
			shader.setFloat("color", 0.0f, (float)Math.sin(glfwGetTime()), 0.0f, 1.0f);
			shader.use();
			
			// render
			mesh.render();
			
			// refresh display
			glfwSwapBuffers(window.getWindow());
			glClear(GL_COLOR_BUFFER_BIT);
		}
		
		// terminate glfw after successful execution
		glfwTerminate();
	}
	
	private static void processInput(long window) {
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window, true);
	}
}