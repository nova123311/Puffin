package com.bird.puffin;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
	private long window;

	public Window(int width, int height) {
		glfwInit();
		window = glfwCreateWindow(width, height, "Puffin", 0, 0);
	}
	
	public long getWindow() {
		return window;
	}
}