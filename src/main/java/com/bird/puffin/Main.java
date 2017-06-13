package com.bird.puffin;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.*;

public class Main {

	public static void main(String[] args) {
		
		// create window
		Window window = new Window(800, 600);
		glfwMakeContextCurrent(window.getWindow());
		
		// allow openGL bindings to be used
		createCapabilities();
		
		// set viewport
		glViewport(0, 0, 800, 600);
		
		// set color to clear background to
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		
		// main game loop
		while (!glfwWindowShouldClose(window.getWindow())) {
			glfwPollEvents();
			glClear(GL_COLOR_BUFFER_BIT);
			glfwSwapBuffers(window.getWindow());
		}
		
		// terminate glfw after successful execution
		glfwTerminate();
	}
}