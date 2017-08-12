package com.bird.puffin;

import org.joml.Vector3f;

public class PointLight {
	private Vector3f position;
	private float constant, linear, quadratic;
	
	public PointLight(Vector3f position, float constant, float linear, float quadratic) {
		this.position = position;
		this.constant = constant;
		this.linear = linear;
		this.quadratic = quadratic;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getConstant() {
		return constant;
	}
	
	public float getLinear() {
		return linear;
	}
	
	public float getQuadratic() {
		return quadratic;
	}
}
