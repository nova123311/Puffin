package com.bird.puffin;

import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.PointerBuffer;

import static org.lwjgl.assimp.Assimp.*;

public class Model {
	private ArrayList<Mesh> meshes;

	public Model(String path) {
		meshes = new ArrayList<Mesh>();
		AIScene aiScene = aiImportFile(path, aiProcess_Triangulate);
		processScene(aiScene);
	}
	
	public void render() {
		for (Mesh mesh : meshes) {
			mesh.render();
		}
	}
	
	private void processScene(AIScene aiScene) {
		PointerBuffer buffer = aiScene.mMeshes();
		for (int i = 0; i < buffer.remaining(); ++i) {
			AIMesh mesh = AIMesh.create(buffer.get(i));
			meshes.add(createMesh(mesh));
		}
	}
	
	private Mesh createMesh(AIMesh mesh) {
		AIVector3D.Buffer vertexBuffer = mesh.mVertices();
		AIVector3D.Buffer normalBuffer = mesh.mNormals();
		AIVector3D.Buffer textureBuffer = mesh.mTextureCoords(0);
		ArrayList<Float> vertexData = new ArrayList<Float>();
		for (int i = 0; i < vertexBuffer.remaining(); ++i) {
			
			// vertex position data
			AIVector3D vertex = vertexBuffer.get(i);
			vertexData.add(vertex.x());
			vertexData.add(vertex.y());
			vertexData.add(vertex.z());
			
			// normal vector data
			AIVector3D normal = normalBuffer.get(i);
			vertexData.add(normal.x());
			vertexData.add(normal.y());
			vertexData.add(normal.z());
			
			// texture coordinate data
			AIVector3D texture = textureBuffer.get(i);
			vertexData.add(texture.x());
			vertexData.add(texture.y());
		}
		
		ArrayList<Integer> indexData = new ArrayList<Integer>();
		AIFace.Buffer faceBuffer = mesh.mFaces();
		while (faceBuffer.hasRemaining()) {
			AIFace aiFace = faceBuffer.get();
			IntBuffer buffer = aiFace.mIndices();
			while (buffer.hasRemaining()) {
				indexData.add(buffer.get());
			}
		}
		
		float[] vertices = new float[vertexData.size()];
		int[] indices = new int[indexData.size()];
		for (int i = 0; i < vertexData.size(); ++i) {
			vertices[i] = vertexData.get(i);
		}
		for (int i = 0; i < indexData.size(); ++i) {
			indices[i] = indexData.get(i);
		}
		
		return new Mesh(vertices, indices);
	}
}
