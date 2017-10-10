package com.bird.puffin;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * This class allows the user to create and reference a GLFW window
 * @author Francis
 */
public class Window {
	private long window;

	/**
	 * Create a glfw window using parameters specified by the user
	 * @param title of the window to display on the top bar
	 * @param width of the window
 	 * @param height of the window
	 */
	public Window(String title, int width, int height) {
		
		// initialize GLFW
		glfwInit();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		// create window
		window = glfwCreateWindow(width, height, title, 0, 0);
		glfwMakeContextCurrent(window);
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}
	
	public void update() {
		glfwPollEvents();
		glfwSwapBuffers(window);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public void setShouldClose(boolean shouldClose) {
		glfwSetWindowShouldClose(window, shouldClose);
	}
	
	/**
	 * Obtain the id representing the window as a long
	 * @return the long value representing the window
	 */
	public long getWindow() {
		return window;
	}
}