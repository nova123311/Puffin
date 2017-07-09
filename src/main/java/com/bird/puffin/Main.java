package com.bird.puffin;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.*;

public class Main {
	static final int WIDTH = 1920;
	static final int HEIGHT = 1080;
	static float deltaTime = 0.0f;
	static float lastFrame = 0.0f;
	
	static float lastX = WIDTH / 2, lastY = HEIGHT / 2;

	public static void main(String[] args) {
		
		// initialize GLFW
		glfwInit();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		// create window
		Window window = new Window("Puffin", WIDTH, HEIGHT);
		glfwMakeContextCurrent(window.getWindow());

		// hide cursor
		glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		// initialize GL context
		createCapabilities();
		glViewport(0, 0, WIDTH, HEIGHT);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		
		// camera
		Camera camera = new Camera(0.0f, 0.0f, 3.0f, 0.05f, 0.0f);
		
		// cube vertex data
		float vertices[] = {
				
				// face 1
			    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
			    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
			    0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
			    0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
			    
			    // face 2
			    -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			    -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			    -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			    -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			    
			    // face 3
			    0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
			    0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
			    -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
			    -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
			    
			    // face 4
			    0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			    0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			    0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			    0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			    
			    // face 5
			    -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
			    -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
			    0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
			    0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
			    
			    // face 6
			    -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
			    -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
			    0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
			    0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f
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
		
		// cube to draw and shader
		Shader shader = new Shader("src/main/resources/shader.vs", "src/main/resources/shader.frag");
		Mesh cube = new Mesh(vertices, indices);
		
		// light to draw and shader
		Shader lightShader = new Shader("src/main/resources/lightshader.vs", "src/main/resources/lightshader.frag");
		Mesh light = new Mesh(vertices, indices);
		Vector3f lightPos = new Vector3f(1.2f, 1.0f, 2.0f);
		
		// main game loop
		while (!glfwWindowShouldClose(window.getWindow())) {
			float currentFrame = (float)glfwGetTime();
			deltaTime = currentFrame - lastFrame;
			lastFrame = currentFrame;
			
			// take inputs
			glfwPollEvents();
			processInput(window.getWindow(), camera);
			
			float[] a = new float[16];
			
			// draw cube
			shader.use();
			Matrix4f model = new Matrix4f();
			Matrix4f view = camera.getViewMatrix();
			Matrix4f projection = new Matrix4f().perspective(0.79f, (float)WIDTH / (float)HEIGHT, 0.1f, 100.0f);
			shader.setFloatMatrix("model", model.get(a));
			shader.setFloatMatrix("view", view.get(a));
			shader.setFloatMatrix("projection", projection.get(a));
			shader.setFloat("objectColor", 1.0f, 0.5f, 0.31f);
			shader.setFloat("lightColor", 1.0f, 1.0f, 1.0f);
			shader.setFloat("lightPos", lightPos.x, lightPos.y, lightPos.z);
			Vector3f position = camera.getPosition();
			shader.setFloat("cameraPos", position.x, position.y, position.z);
			cube.render();
			
			// draw light
			lightShader.use();
			model.identity();
			model.translate(lightPos);
			model.scale(0.2f);
			lightShader.setFloatMatrix("model", model.get(a));
			lightShader.setFloatMatrix("view", view.get(a));
			lightShader.setFloatMatrix("projection", projection.get(a));
			light.render();
			
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
		camera.setSpeed(speed);
		if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
			camera.translate(Camera.Direction.FRONT);
		if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
			camera.translate(Camera.Direction.BACK);
		if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
			camera.translate(Camera.Direction.LEFT);
		if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
			camera.translate(Camera.Direction.RIGHT);
		
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