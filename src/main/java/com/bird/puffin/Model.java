package com.bird.puffin;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.PointerBuffer;

import static org.lwjgl.assimp.Assimp.*;

public class Model {
	private String directory;
	private ArrayList<Mesh> meshes;
	private ArrayList<Material> materials;
	private ArrayList<Texture> diffuseTextures;
	private ArrayList<Texture> specularTextures;

	public Model(String directory, String model) {
		this.directory = directory;
		meshes = new ArrayList<Mesh>();
		materials = new ArrayList<Material>();
		diffuseTextures = new ArrayList<Texture>();
		specularTextures = new ArrayList<Texture>();
		AIScene aiScene = aiImportFile(directory + model, aiProcess_Triangulate | aiProcess_GenSmoothNormals
				| aiProcess_JoinIdenticalVertices | aiProcess_SplitLargeMeshes);
		createMaterials(aiScene);
		processScene(aiScene);
	}
	
	/**
	 * Render the model by iteratively rendering each of its meshes
	 * @param shader to use while rendering the model
	 */
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
			
			// diffuse textures
			AIString path = AIString.create();
			IntBuffer test = IntBuffer.allocate(1);
			FloatBuffer test2 = FloatBuffer.allocate(1);
			aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path, test, test, test2, test, test, test);
			if (path.dataString().contains(".")) {
				diffuseTextures.add(new Texture(directory + path.dataString()));
			}
			
			// specular textures
			aiGetMaterialTexture(aiMaterial, aiTextureType_SPECULAR, 0, path, test, test, test2, test, test, test);
			if (path.dataString().contains(".")) {
				specularTextures.add(new Texture(directory + path.dataString()));
			}
		}
	}
	
	private Mesh createMesh(AIMesh aiMesh) {
		
		// get vertices of the mesh
		AIVector3D.Buffer vertexBuffer = aiMesh.mVertices();
		AIVector3D.Buffer normalBuffer = aiMesh.mNormals();
		AIVector3D.Buffer textureBuffer = aiMesh.mTextureCoords(0);
		Vertex[] vertices = new Vertex[vertexBuffer.remaining()];
		for (int i = 0; i < vertexBuffer.remaining(); ++i) {
			if (textureBuffer != null) {
				vertices[i] = new Vertex(vertexBuffer.get(i), normalBuffer.get(i), textureBuffer.get(i));
			} else {
				vertices[i] = new Vertex(vertexBuffer.get(i), normalBuffer.get(i), null);
			}
		}
		
		// get indices of the mesh
		ArrayList<Integer> indexData = new ArrayList<Integer>();
		AIFace.Buffer faceBuffer = aiMesh.mFaces();
		while (faceBuffer.hasRemaining()) {
			IntBuffer buffer = faceBuffer.get().mIndices();
			while (buffer.hasRemaining()) {
				indexData.add(buffer.get());
			}
		}
		int[] indices = new int[indexData.size()];
		for (int i = 0; i < indexData.size(); ++i) {
			indices[i] = indexData.get(i);
		}
		
		// get mesh material
		Material material = materials.get(aiMesh.mMaterialIndex());
		
		// get mesh diffuse texture
		Texture diffuseTexture = diffuseTextures.get(aiMesh.mMaterialIndex() - 1);
		
		// get mesh specular texture
		Texture specularTexture = specularTextures.get(aiMesh.mMaterialIndex() - 1);
		
		return new Mesh(vertices, indices, material, diffuseTexture, specularTexture);
	}
	
	private void processScene(AIScene aiScene) {
		PointerBuffer buffer = aiScene.mMeshes();
		for (int i = 0; i < buffer.remaining(); ++i) {
			AIMesh mesh = AIMesh.create(buffer.get(i));
			meshes.add(createMesh(mesh));
		}
	}
}
