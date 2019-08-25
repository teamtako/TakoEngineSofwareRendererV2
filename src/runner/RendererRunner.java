package runner;

import java.util.ArrayList;

import Utilities.ObjReader;
import engine.Renderer;
import math.Vector2;
import math.Vector3;
import math.Vector4;

public class RendererRunner {
	public static void main(String[] args) {
		Renderer renderer = new Renderer(800, 500);
		renderer.setClearColor(0.5f, 0.6f, 0.9f, 1);
		renderer.setPointSize(8);
		renderer.setLineSize(4);

		float deltaTime = 0;
		long endTime = 0;
		long startTime = System.currentTimeMillis();
		while (true) {
			renderer.clearBackBuffer();
			renderer.setPointSize(1);
			ObjReader objReader= new ObjReader();
			ArrayList<Vector3> vectors = objReader.getVerticesFromObjFile("/Users/league/Desktop/TakoEngineSofwareRendererV2/src/Utilities/monkey.obj");
			renderer.renderVertecies(vectors);
			
			
			renderer.swapBuffers();
			
			endTime = System.currentTimeMillis();
			deltaTime = (float)(endTime - startTime) / 1000.0f;
			startTime = endTime;
		}
	}
}
