package com.bird.puffin;

import org.lwjgl.assimp.AIColor4D;
import org.joml.Vector3f;

public class Material {
	private Vector3f ambient;
	private Vector3f diffuse;
	private Vector3f specular;
	private float shininess;

	public Material(AIColor4D ambient, AIColor4D diffuse, AIColor4D specular, float shininess) {
		this.ambient = new Vector3f(ambient.r(), ambient.g(), ambient.b());
		this.diffuse = new Vector3f(diffuse.r(), diffuse.g(), diffuse.b());
		this.specular = new Vector3f(specular.r(), specular.g(), specular.b());
		this.shininess = shininess;
	}
	
	public Vector3f getAmbient() {
		return ambient;
	}
	
	public Vector3f getDiffuse() {
		return diffuse;
	}
	
	public Vector3f getSpecular() {
		return specular;
	}
	
	public float getShininess() {
		return shininess;
	}
}
