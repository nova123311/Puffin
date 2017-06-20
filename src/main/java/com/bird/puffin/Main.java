package com.bird.puffin;

import org.joml.Math;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.*;

public class Main {

	public static void main(String[] args) {
		
		// initialize GLFW
		glfwInit();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		// create window
		Window window = new Window("Puffin", 800, 600);
		glfwMakeContextCurrent(window.getWindow());
		
		// initialize GL context
		createCapabilities();
		glViewport(0, 0, 800, 600);
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		
		// create shader
		Shader shader = new Shader("src/main/resources/shader.vs", "src/main/resources/shader.frag");
		
		// triangle vertex data
		float[] vertices = {
				-0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
				-0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
				0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f
		};
		int[] indices = {
				0, 1, 2,
				0, 2, 3
		};
		
		// create mesh to draw
		Mesh mesh = new Mesh(vertices, indices);
		
		// create texture
		Texture texture = new Texture();
		texture.load("src/main/resources/container.jpg");
		texture.load("src/main/resources/anime.png");
		
		// main game loop
		while (!glfwWindowShouldClose(window.getWindow())) {
			
			// take inputs
			glfwPollEvents();
			processInput(window.getWindow());
			
			// shader attributes
			shader.use();
			shader.setInt("ourTexture1", 0);
			shader.setInt("ourTexture2", 1);
			float time = (float)Math.abs(Math.sin(glfwGetTime()));
			shader.setFloat("transparency", time, time, time, time);
			
			// transforms
			Matrix4f trans = new Matrix4f();
			float[] a = new float[16];
			trans.rotate((float)glfwGetTime(), 0.0f, 1.0f, 1.0f);
			trans.scale(0.75f);
			shader.setMatrix("transform", trans.get(a));
			
			// texture
			texture.use();
			
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