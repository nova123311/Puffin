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
		
		// window
		Window window = new Window("Puffin", WIDTH, HEIGHT);
		
		// camera
		Camera camera = new Camera(0.0f, 0.0f, 3.0f, 0.05f, 0.0f);
		
		// controller for managing user input
		Controller controller = new Controller(window, camera);
		
		// initialize GL context
		createCapabilities();
		glViewport(0, 0, WIDTH, HEIGHT);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		
		// terrain to render
		Shader terrainShader = new Shader("src/main/resources/shader.vs", "src/main/resources/shader.frag");
		Terrain terrain = new Terrain();
		
		// model to draw and shader
		Shader shader = new Shader("src/main/resources/shaders/shader.vs", 
				"src/main/resources/shaders/shader.frag");
		Model object = new Model("src/main/resources/suit/", "nanosuit.obj");
		
		// create a point light
		Shader lightShader = new Shader("src/main/resources/shaders/lightshader.vs", 
				"src/main/resources/shaders/lightshader.frag");
		Model light = new Model("src/main/resources/cube/", "cube.obj");	
		PointLight pointLight = new PointLight(new Vector3f(1.2f, 1.0f, 2.0f), 1.0f, 0.09f, 0.032f);
		
		// main rendering loop
		float[] a = new float[16];
		Matrix4f projection = new Matrix4f().perspective(0.79f, (float)WIDTH / (float)HEIGHT, 0.01f, 100.0f);
		while (!window.shouldClose()) {
			
			// take inputs
			glfwPollEvents();
			controller.processKeyboardInput();
			controller.processMouseInput();
			
			// draw terrain
			Matrix4f model = new Matrix4f();
			Matrix4f view = camera.getViewMatrix();
			terrainShader.use();
			model.scale(0.1f);
			terrainShader.setFloatMatrix("model", model.get(a));
			terrainShader.setFloatMatrix("view", view.get(a));
			terrainShader.setFloatMatrix("projection", projection.get(a));
			terrain.render(terrainShader);
			
			// draw model
			/*
			shader.use();
			Matrix4f model = new Matrix4f();
			Matrix4f view = camera.getViewMatrix();
			for (int i = 0; i < 10; ++i) {
				model.identity().translate(-i, -i, -i).scale(0.2f);
				shader.setFloatMatrix("model", model.get(a));
				shader.setFloatMatrix("view", view.get(a));
				shader.setFloatMatrix("projection", projection.get(a));
				Vector3f position = camera.getPosition();
				shader.setFloat("cameraPos", position.x, position.y, position.z);
				for (int j = 0; j < 1; ++j) {
					shader.setFloat("pointLights[" + j + "].ambient", 0.2f, 0.2f, 0.2f);
					shader.setFloat("pointLights[" + j + "].diffuse", 1.0f, 1.0f, 1.0f);
					shader.setFloat("pointLights[" + j + "].specular", 1.0f, 1.0f, 1.0f);
					shader.setFloat("pointLights[" + j + "].constant", pointLight.getConstant());
					shader.setFloat("pointLights[" + j + "].linear", pointLight.getLinear());
					shader.setFloat("pointLights[" + j + "].quadratic", pointLight.getQuadratic());
					position = pointLight.getPosition();
					shader.setFloat("pointLights[" + j + "].position", position.x, position.y, position.z);
				}
				object.render(shader);
			}
			*/
			
			// draw light
			lightShader.use();
			model.identity().translate(pointLight.getPosition()).scale(0.2f);
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