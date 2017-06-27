package com.bird.puffin;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private Vector3f position;
	private Vector3f target;
	private Vector3f front;
	private Vector3f up;
	private Vector3f right;
	
	private float yaw;
	private float pitch;
	
	public enum Direction {
		FRONT, BACK, LEFT, RIGHT
	}

	public Camera() {
		position = new Vector3f(0.0f, 0.0f, 3.0f);
		target = new Vector3f(0.0f, 0.0f, 0.0f);
		front = new Vector3f(0.0f, 0.0f, -1.0f);
		up = new Vector3f(0.0f, 1.0f, 0.0f);
		right = new Vector3f(1.0f, 0.0f, 0.0f);
		
		yaw = -90.0f;
		pitch = 0.0f;
	}
	
	public Matrix4f getViewMatrix() {
		Matrix4f view = new Matrix4f();
		position.add(front, target);
		return view.lookAt(position, target, up);
	}
	
	public void translate(Direction direction, float speed) {
		if (direction == Direction.FRONT) {
			position.add(front.mul(speed));
		} else if (direction == Direction.BACK) {
			position.sub(front.mul(speed));
		} else if (direction == Direction.LEFT) {
			position.sub(right.mul(speed));
		} else if (direction == Direction.RIGHT) {
			position.add(right.mul(speed));
		}
		update();
	}
	
	public void look(float xOffset, float yOffset) {
		yaw += xOffset * 0.1f;
		pitch += yOffset * 0.1f;
		update();
	}
	
	private void update() {
		front.set((float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))),
				(float)Math.sin(Math.toRadians(pitch)),
				(float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)))).normalize();
		front.cross(new Vector3f(0.0f, 1.0f, 0.0f), right).normalize(right);
		right.cross(front, up).normalize(up);
	}
}
