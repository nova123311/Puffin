package com.bird.puffin;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private Matrix4f view;
	private Vector3f position;
	private Vector3f target;

	public Camera(float posX, float posY, float posZ, float tarX, float tarY, float tarZ) {
		view = new Matrix4f();
		position = new Vector3f(posX, posY, posZ);
		target = new Vector3f(tarX, tarY, tarZ);
	}
	
	public Matrix4f getViewMatrix() {
		view.identity();
		return view.lookAt(position, target, new Vector3f(0.0f, 1.0f, 0.0f));
	}
	
	public void translate(float x, float y, float z) {
		position.add(x, y, z);
		target.add(x, y, z);
	}
}
