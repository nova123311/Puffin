package com.bird.puffin;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	private int shader;

	public Shader(String vertexShaderPath, String fragmentShaderPath) {
		
		// compile vertex shader
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, readPath(vertexShaderPath));
		glCompileShader(vertexShader);
		
		// compile fragment shader
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, readPath(fragmentShaderPath));
		glCompileShader(fragmentShader);
		
		// create shader program and link
		shader = glCreateProgram();
		glAttachShader(shader, vertexShader);
		glAttachShader(shader, fragmentShader);
		glLinkProgram(shader);
	}
	
	public void use() {
		glUseProgram(shader);
	}
	
	private String readPath(String path) {
		String result = new String();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready())
				result = result + "\n" + br.readLine();
			br.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}