package com.bird.puffin;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	private int program;

	public Shader(String vertexShaderPath, String fragmentShaderPath) {
		
		// compile vertex/fragment shader
		int vertexShader = compileShader(GL_VERTEX_SHADER, vertexShaderPath);
		int fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentShaderPath);
		
		// create shader program and link
		program = glCreateProgram();
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		glLinkProgram(program);
	}
	
	public void use() {
		glUseProgram(program);
	}
	
	public void setFloat(String name, float a, float b, float c, float d) {
		int location = glGetUniformLocation(program, name);
		glUniform4f(location, a, b, c, d);
	}
	
	private int compileShader(int shaderType, String shaderPath) {
		int shader = glCreateShader(shaderType);
		glShaderSource(shader, readPath(shaderPath));
		glCompileShader(shader);
		return shader;
	}
	
	private String readPath(String path) {
		String result = new String();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready())
				result = result + "\n" + br.readLine();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}