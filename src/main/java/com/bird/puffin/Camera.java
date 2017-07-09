package com.bird.puffin;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * The camera class encapsulates the functions of a camera such as moving
 * and looking around. 
 * @author Francis
 */
public class Camera {
	private Vector3f position;
	private Vector3f front;
	private Vector3f up;
	private Vector3f right;	
	private float yaw;
	private float pitch;
	private float sensitivity;
	private float speed;
	
	public enum Direction {
		FRONT, BACK, LEFT, RIGHT
	}

	/**
	 * Construct a new camera at given position, sensitivity, and speed
	 * @param x coordinate of camera
	 * @param y coordinate of camera
	 * @param z coordinate of camera
	 * @param sensitivity of camera when looking around
	 * @param speed of camera when moving around
	 */
	public Camera(float x, float y, float z, float sensitivity, float speed) {
		position = new Vector3f(x, y, z);
		front = new Vector3f();
		up = new Vector3f();
		right = new Vector3f();
		yaw = -90.0f;
		pitch = 0.0f;
		this.sensitivity = sensitivity;
		this.speed = speed;
		update();
	}
	
	/**
	 * Get view matrix to transform from world space to view space
	 * @return the camera's view matrix
	 */
	public Matrix4f getViewMatrix() {
		Matrix4f view = new Matrix4f();
		Vector3f target = new Vector3f();
		position.add(front, target);
		return view.lookAt(position, target, up);
	}
	
	/**
	 * The camera "moves" around by translating in a direction at speed
	 * @param direction to move in
	 */
	public void translate(Direction direction) {
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
	
	/**
	 * The camera "looks" around by offsets on the x and/or y axis
	 * @param xOffset is the change in yaw
	 * @param yOffset is the change in pitch
	 */
	public void look(float xOffset, float yOffset) {
		yaw += xOffset * sensitivity;
		pitch += yOffset * sensitivity;
		if (pitch > 89.9f)
			pitch = 89.9f;
		else if (pitch < -89.9f)
			pitch = -89.9f;
		update();
	}
	
	/**
	 * Obtain the position of the camera as a vector of 3 components (floats)
	 * @return the position of the camera
	 */ 
	public Vector3f getPosition() {
		return position;
	}
	
	/**
	 * Adjust speed of the camera
	 * @param speed to set camera to
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	/**
	 * Update the camera's local (front, right, up) vectors
	 */
	private void update() {
		front.set((float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))),
				(float)Math.sin(Math.toRadians(pitch)),
				(float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)))).normalize();
		front.cross(new Vector3f(0.0f, 1.0f, 0.0f), right).normalize(right);
		right.cross(front, up).normalize(up);
	}
}
