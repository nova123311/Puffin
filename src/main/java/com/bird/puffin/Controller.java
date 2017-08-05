package com.bird.puffin;

import static org.lwjgl.glfw.GLFW.*;

public class Controller {
	private Window window;
	private Camera camera;

	private static float deltaTime = 0.0f;
	private static float lastFrame = 0.0f;
	private static float lastX = 1920 / 2, lastY = 1080 / 2;

	public Controller(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;
	}
	
	public void processKeyboardInput() {
		
		// quit application
		if (glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
			window.setShouldClose(true);
		
		float currentFrame = (float)glfwGetTime();
		deltaTime = currentFrame - lastFrame;
		lastFrame = currentFrame;
		
		// moving around
		float speed = 5f * deltaTime;
		camera.setSpeed(speed);
		if (glfwGetKey(window.getWindow(), GLFW_KEY_W) == GLFW_PRESS)
			camera.translate(Camera.Direction.FRONT);
		if (glfwGetKey(window.getWindow(), GLFW_KEY_S) == GLFW_PRESS)
			camera.translate(Camera.Direction.BACK);
		if (glfwGetKey(window.getWindow(), GLFW_KEY_A) == GLFW_PRESS)
			camera.translate(Camera.Direction.LEFT);
		if (glfwGetKey(window.getWindow(), GLFW_KEY_D) == GLFW_PRESS)
			camera.translate(Camera.Direction.RIGHT);
	}
	
	public void processMouseInput() {
		
		// looking around
		double[] xpos = new double[1];
		double[] ypos = new double[1];
		glfwGetCursorPos(window.getWindow(), xpos, ypos);
		float xOffset = (float)(xpos[0] - lastX);
		float yOffset = (float)(lastY - ypos[0]);
		lastX = (float)xpos[0];
		lastY = (float)ypos[0];
		camera.look(xOffset, yOffset);
	}
}
