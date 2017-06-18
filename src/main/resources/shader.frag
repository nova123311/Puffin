#version 330 core

in vec2 TexCoord;
in vec3 ourColor;
out vec4 FragColor;

uniform sampler2D ourTexture1;
uniform sampler2D ourTexture2;
uniform vec4 transparency;

void main() {
	FragColor = mix(texture(ourTexture1, TexCoord), texture(ourTexture2, TexCoord), transparency);
}