package com.bird.puffin;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.lwjgl.opengl.GL20.*;

/**
 * Shader is the class for creating shader programs from a vertex shader
 * and a fragment shader. The shader should be called (used) before it affects
 * rendering.
 * @author Francis
 */
public class Shader {
	private int program;

	/**
	 * Construct a new shader program from a vertex shader and a fragment
	 * shader
	 * @param vertexShaderPath path to the vertex shader file
	 * @param fragmentShaderPath path to the fragment shader file
	 */
	public Shader(String vertexShaderPath, String fragmentShaderPath) {
		
		// compile vertex/fragment shader
		int vertexShader = compileShader(GL_VERTEX_SHADER, vertexShaderPath);
		int fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentShaderPath);
		
		// create shader program and link
		program = glCreateProgram();
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		glLinkProgram(program);
		System.err.println(glGetProgramInfoLog(program));
	}
	
	/**
	 * Use the shader program
	 */
	public void use() {
		glUseProgram(program);
	}
	
	/**
	 * Set the matrix uniform variable to a specified matrix in OpenGL's
	 * matrix format
	 * @param name of the uniform variable to set
	 * @param matrix to set the uniform variable to
	 */
	public void setFloatMatrix(String name, float[] matrix) {
		int location = glGetUniformLocation(program, name);
		if (matrix.length == 4) {
			glUniformMatrix2fv(location, false, matrix);
		} else if (matrix.length == 9) {
			glUniformMatrix3fv(location, false, matrix);
		} else if (matrix.length == 16) {
			glUniformMatrix4fv(location, false, matrix);
		}
	}	
	
	/**
	 * Set specified uniform variable to int values given
	 * @param name of uniform variable to set
	 * @param values to set uniform variable components to
	 */
	public void setInt(String name, int... values) {
		int location = glGetUniformLocation(program, name);
		if (values.length == 1) {
			glUniform1i(location, values[0]);
		} else if (values.length == 2) {
			glUniform2i(location, values[0], values[1]);
		} else if (values.length == 3) {
			glUniform3i(location, values[0], values[1], values[2]);
		} else if (values.length == 4) {
			glUniform4i(location, values[0], values[1], values[2], values[3]);
		}
	}

	/**
	 * Set specified uniform variable to float values given
	 * @param name of uniform variable to set
	 * @param values to set uniform variable components to
	 */
	public void setFloat(String name, float... values) {
		int location = glGetUniformLocation(program, name);
		if (values.length == 1) {
			glUniform1f(location, values[0]);
		} else if (values.length == 2) {
			glUniform2f(location, values[0], values[1]);
		} else if (values.length == 3) {
			glUniform3f(location, values[0], values[1], values[2]);
		} else if (values.length == 4) {
			glUniform4f(location, values[0], values[1], values[2], values[3]);
		}
	}
	
	/**
	 * Compile a shader of specified type from a specified type and return
	 * the shader 
	 * @param shaderType type of shader to compile
	 * @param shaderPath path to the shader file to open
	 * @return the compiled shader
	 */
	private int compileShader(int shaderType, String shaderPath) {
		int shader = glCreateShader(shaderType);
		glShaderSource(shader, readPath(shaderPath));
		glCompileShader(shader);
		System.err.println(glGetShaderInfoLog(shader));
		return shader;
	}
	
	/**
	 * Returns the contents of a file as a string
	 * @param path to the file to open
	 * @return the contents of the file as a string
	 */
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