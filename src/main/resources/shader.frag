#version 330 core

// location of the vertex, the vertex normal, and vertex texture coordinates
// sent from the vertex shader
in vec3 FragPos;
in vec3 Normal;
in vec2 TexCoords;

// output color of the fragment
out vec4 FragColor;

// properties of scene needed calculate shading on vertex
uniform vec3 objectColor;
uniform vec3 lightColor;
uniform vec3 cameraPos;

// structure of a material contains its diffuse texture, its specular texture,
// and its shininess
struct Material {
	sampler2D diffuse;
	sampler2D specular;
	float shininess;
};
uniform Material material;

// structure of a point light contains its positions, its phong shading
// model colors (ambient, diffuse, specular), and its attenuation factors
// (constant, linear, quadratic)
struct PointLight {
	vec3 position;
	
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
	
	float constant;
	float linear;
	float quadratic;
};
#define POINT_LIGHT_COUNT 1
uniform PointLight pointLights[POINT_LIGHT_COUNT];

vec3 calcPointLight(PointLight pointLight, vec3 fragPos, vec3 normal, vec3 viewDir);

void main() {
	vec3 result = vec3(0.0f);

	// calculate effect of all point lights in the scene
	for (int i = 0; i < POINT_LIGHT_COUNT; ++i) {
		result += calcPointLight(pointLights[i], FragPos, normalize(Normal),
				normalize(cameraPos - FragPos));
	}
	
	FragColor = vec4(result, 1.0f);
}

// calculate point light effect on fragment
vec3 calcPointLight(PointLight pointLight, vec3 fragPos, vec3 normal, vec3 viewDir) {
	vec3 lightDir = normalize(pointLight.position - fragPos);
	
	// calculate attenuation of the point light
	float distance = length(pointLight.position - fragPos);
	float attenuation = 1.0 / (pointLight.constant + pointLight.linear * distance +
			pointLight.quadratic * (distance * distance));
	
	// ambient shading component of the point light
	vec3 ambient = attenuation * pointLight.ambient * vec3(texture(material.diffuse, TexCoords));
	
	// diffuse shading component of the point light
	float diff = max(dot(normal, lightDir), 0.0f);
	vec3 diffuse = attenuation * pointLight.diffuse * diff * vec3(texture(material.diffuse, TexCoords));
	
	// specular shading component of the point light 
	vec3 reflectDir = reflect(-lightDir, normal);
	float spec = pow(max(dot(viewDir, reflectDir), 0.0f), material.shininess);
	vec3 specular = attenuation * pointLight.specular * spec * vec3(texture(material.specular, TexCoords));
	
	// effect of the point light is all shading components added together
	return ambient + diffuse + specular;
}