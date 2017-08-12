package com.bird.puffin;

import org.lwjgl.assimp.AIVector3D;

/**
 * 
 * @author Francis
 */
public class Vertex {
	private float[] vertex;

	public Vertex(AIVector3D position, AIVector3D normal, AIVector3D texCoord) {
		vertex = new float[8];
		
		// vertex position
		vertex[0] = position.x();
		vertex[1] = position.y();
		vertex[2] = position.z();
		
		// vertex normal
		vertex[3] = normal.x();
		vertex[4] = normal.y();
		vertex[5] = normal.z();
		
		// vertex texture coordinates
		if (texCoord != null) {
			vertex[6] = texCoord.x();
			vertex[7] = texCoord.y();
		}
	}
	
	public float[] getVertex() {
		return vertex;
	}
}
