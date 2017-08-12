package com.bird.puffin;

import static org.lwjgl.glfw.GLFW.*;

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
		window = glfwCreateWindow(width, height, title, 0, 0);
		glfwMakeContextCurrent(window);
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
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