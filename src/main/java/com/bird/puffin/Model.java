package com.bird.puffin;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.PointerBuffer;

import static org.lwjgl.assimp.Assimp.*;

public class Model {
	private ArrayList<Mesh> meshes;
	private ArrayList<Material> materials;

	public Model(String path) {
		meshes = new ArrayList<Mesh>();
		materials = new ArrayList<Material>();
		AIScene aiScene = aiImportFile(path, aiProcess_Triangulate | aiProcess_GenNormals);
		createMaterials(aiScene);
		processScene(aiScene);
	}
	
	public void render(Shader shader) {
		for (Mesh mesh : meshes) {
			mesh.render(shader);
		}
	}
	
	private void createMaterials(AIScene aiScene) {
		
		// material (ambient, diffuse, specular) data
		PointerBuffer materialBuffer = aiScene.mMaterials();
		while (materialBuffer.hasRemaining()) {
			AIMaterial aiMaterial = AIMaterial.create(materialBuffer.get());
			AIColor4D ambient = AIColor4D.create();
			AIColor4D diffuse = AIColor4D.create();
			AIColor4D specular = AIColor4D.create();
			AIColor4D shininess = AIColor4D.create();
			aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, ambient);
			aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, diffuse);
			aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, specular);
			aiGetMaterialColor(aiMaterial, AI_MATKEY_SHININESS, aiTextureType_NONE, 0, shininess);
			materials.add(new Material(ambient, diffuse, specular, shininess.r()));
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
			if (textureBuffer != null) {
				AIVector3D texture = textureBuffer.get(i);
				vertexData.add(texture.x());
				vertexData.add(texture.y());
			} else {
				vertexData.add(0f);
				vertexData.add(0f);
			}
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
		
		return new Mesh(vertices, indices, materials.get(mesh.mMaterialIndex()));
	}
}
