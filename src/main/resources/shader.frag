#version 330 core

out vec4 color;

uniform vec3 terrainColor;
uniform vec3 lightColor;

void main() {

	// calculate ambient component
	vec3 ambient = 0.1f * lightColor;
	
	// calculate diffuse component
	
	// resulting color is sum of ambient, diffuse, specular components
	vec3 result = ambient * terrainColor;
	color = vec4(result, 1.0f);
}