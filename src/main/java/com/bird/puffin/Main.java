package com.bird.puffin;

import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.*;

public class Main {
	static float deltaTime = 0.0f;
	static float lastFrame = 0.0f;
	
	static float lastX = 400, lastY = 300;

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
		
		// hide cursor
		glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		// initialize GL context
		createCapabilities();
		glViewport(0, 0, 800, 600);
		glClearColor(1.0f, 0.71f, 0.76f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		
		// camera
		Camera camera = new Camera();
		
		// create shader
		Shader shader = new Shader("src/main/resources/shader.vs", "src/main/resources/shader.frag");
		
		// cube vertex data
		float vertices[] = {
				
				// face 1
			    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			    0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			    0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			    
			    // face 2
			    -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			    -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			    
			    // face 3
			    0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			    0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			    -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			    -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			    
			    // face 4
			    0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			    0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			    0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			    0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			    
			    // face 5
			    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			    -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			    0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			    0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			    
			    // face 6
			    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			    -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			    0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			    0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f
			};
		int[] indices = {
				
				// face1
				0, 1, 2,
				0, 2, 3,
				
				// face2
				4, 5, 6,
				4, 6, 7,
				
				// face 3
				8, 9, 10,
				8, 10, 11,
				
				// face 4
				12, 13, 14,
				12, 14, 15,
				
				// face 5
				16, 17, 18,
				16, 18, 19,
				
				// face 6
				20, 21, 22,
				20, 22, 23
		};
		
		// create mesh to draw
		Mesh mesh = new Mesh(vertices, indices);
		
		// create texture
		Texture texture = new Texture();
		texture.load("src/main/resources/grill.jpg");
		texture.load("src/main/resources/anime.png");
		
		// main game loop
		while (!glfwWindowShouldClose(window.getWindow())) {
			float currentFrame = (float)glfwGetTime();
			deltaTime = currentFrame - lastFrame;
			lastFrame = currentFrame;
			
			// take inputs
			glfwPollEvents();
			processInput(window.getWindow(), camera);
			
			// shader attributes
			shader.use();
			shader.setInt("ourTexture1", 0);
			shader.setInt("ourTexture2", 1);
			
			// transforms
			Matrix4f model = new Matrix4f();
			Matrix4f view = camera.getViewMatrix();
			Matrix4f projection = new Matrix4f().perspective(0.79f, 1.33f, 0.1f, 100.0f);
			float[] a = new float[16];
			shader.setMatrix("model", model.get(a));
			shader.setMatrix("view", view.get(a));
			shader.setMatrix("projection", projection.get(a));
			
			// texture
			texture.use();
			
			// render
			mesh.render();
			
			// refresh display
			glfwSwapBuffers(window.getWindow());
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		}
		
		// terminate glfw after successful execution
		glfwTerminate();
	}
	
	private static void processInput(long window, Camera camera) {
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window, true);
		
		// moving around
		float speed = 5f * deltaTime;
		if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
			camera.translate(Camera.Direction.FRONT, speed);
		if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
			camera.translate(Camera.Direction.BACK, speed);
		if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
			camera.translate(Camera.Direction.LEFT, speed);
		if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
			camera.translate(Camera.Direction.RIGHT, speed);
		
		// looking around
		double[] xpos = new double[1];
		double[] ypos = new double[1];
		glfwGetCursorPos(window, xpos, ypos);
		float xOffset = (float)(xpos[0] - lastX);
		float yOffset = (float)(lastY - ypos[0]);
		lastX = (float)xpos[0];
		lastY = (float)ypos[0];
		camera.look(xOffset, yOffset);
	}
}