#version 330 core

in vec2 TexCoords;
in vec3 Normal;
in vec3 FragPos;
out vec4 FragColor;

uniform vec3 objectColor;
uniform vec3 lightColor;
uniform vec3 cameraPos;

struct Material {
	sampler2D diffuse;
	sampler2D specular;
	float shininess;
};

uniform Material material;

struct Light {
	vec3 position;
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

uniform Light light;

void main() {

	// ambient lighting
	vec3 ambient = light.ambient * vec3(texture(material.diffuse, TexCoords));
	
	// diffuse lighting
	vec3 norm = normalize(Normal);
	vec3 lightDir = normalize(light.position - FragPos);
	float diff = max(dot(norm, lightDir), 0.0f);
	vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, TexCoords));
	
	// specular lighting
	vec3 viewDir = normalize(cameraPos - FragPos);
	vec3 reflectDir = reflect(-lightDir, norm);
	float spec = pow(max(dot(viewDir, reflectDir), 0.0f), material.shininess);
	vec3 specular = light.specular * spec * vec3(texture(material.specular, TexCoords));
	
	// result of lighting calculations
	vec3 result = ambient + diffuse + specular;
	
	FragColor = vec4(result, 1.0f);
}