package com.bird.puffin;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
	private long window;

	public Window(String title, int width, int height) {
		window = glfwCreateWindow(width, height, title, 0, 0);
	}
	
	public long getWindow() {
		return window;
	}
}