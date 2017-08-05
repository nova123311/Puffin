package com.bird.puffin;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL.*;

public class Main {
	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;

	public static void main(String[] args) {
		
		// initialize GLFW
		glfwInit();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		// create window
		Window window = new Window("Puffin", WIDTH, HEIGHT);

		// hide cursor
		window.setCursorVisible(false);
		
		// initialize GL context
		createCapabilities();
		glViewport(0, 0, WIDTH, HEIGHT);
		glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		
		// camera
		Camera camera = new Camera(0.0f, 0.0f, 3.0f, 0.05f, 0.0f);
		
		// controller for managing user input
		Controller controller = new Controller(window, camera);
		
		// model to draw and shader
		Shader shader = new Shader("src/main/resources/shader.vs", "src/main/resources/shader.frag");
		Model object = new Model("src/main/resources/suit/", "nanosuit.obj");
		
		// light to draw and shader
		Shader lightShader = new Shader("src/main/resources/lightshader.vs", "src/main/resources/lightshader.frag");
		Model light = new Model("src/main/resources/cube/", "cube.obj");
		Vector3f lightPos = new Vector3f((float)(2.33 * Math.sin(glfwGetTime())), 1.0f, 
				(float)(2.33 * Math.cos(glfwGetTime())));
		
		// main game loop
		float[] a = new float[16];
		Matrix4f projection = new Matrix4f().perspective(0.79f, (float)WIDTH / (float)HEIGHT, 0.01f, 100.0f);
		while (!window.shouldClose()) {
			
			// take inputs
			glfwPollEvents();
			controller.processKeyboardInput();
			controller.processMouseInput();
			
			// draw model
			shader.use();
			Matrix4f model = new Matrix4f().scale(0.2f);
			Matrix4f view = camera.getViewMatrix();
			shader.setFloatMatrix("model", model.get(a));
			shader.setFloatMatrix("view", view.get(a));
			shader.setFloatMatrix("projection", projection.get(a));
			Vector3f position = camera.getPosition();
			shader.setFloat("cameraPos", position.x, position.y, position.z);
			shader.setFloat("light.ambient", 0.2f, 0.2f, 0.2f);
			shader.setFloat("light.diffuse", 1.0f, 1.0f, 1.0f);
			shader.setFloat("light.specular", 1.0f, 1.0f, 1.0f);
			shader.setFloat("light.position", lightPos.x, lightPos.y, lightPos.z);
			object.render(shader);
			
			// draw light
			lightShader.use();
			lightPos.set((float)(2.33 * Math.sin(glfwGetTime())), 1.0f, 
					(float)(2.33 * Math.cos(glfwGetTime())));
			model.identity().translate(lightPos).scale(0.2f);
			lightShader.setFloatMatrix("model", model.get(a));
			lightShader.setFloatMatrix("view", view.get(a));
			lightShader.setFloatMatrix("projection", projection.get(a));
			light.render(lightShader);
			
			// refresh display
			glfwSwapBuffers(window.getWindow());
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		}
		
		// terminate glfw after successful execution
		glfwTerminate();
	}
}