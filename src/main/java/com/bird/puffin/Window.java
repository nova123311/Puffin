package com.bird.puffin;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
	private long window;

	public Window(int width, int height) {
		glfwInit();
		window = glfwCreateWindow(width, height, "Puffin", 0, 0);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
	}
	
	public long getWindow() {
		return window;
	}
}