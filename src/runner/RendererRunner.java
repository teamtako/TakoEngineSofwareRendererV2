package runner;

import engine.Renderer;
import math.Quaternion;
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
			Vector4 c1 = new Vector4(0, 1, 0, 1);
			Vector4 c2 = new Vector4(1, 0, 0, 1);
			Vector4 c3 = new Vector4(0, 0, 1, 1);

			Vector3 p13 = new Vector3(-0.5f, -0.5f, 0f);
			Vector3 p23 = new Vector3(0.0f, 0.5f, 0f);
			Vector3 p33 = new Vector3(0.5f, -0.5f, 0f);

			Camera cam = new Camera();
			cam.setPerspectiveProjection(70.0f, (800f / 500f), 0.001f, 1000.0f);
			cam.position = new Vector3(0, 0, -30);
			cam.updateView(deltaTime);

			renderer.renderTriangle3D(p13, p23, p33, c1, c2, c3, cam);
			renderer.setPointSize(1);
			renderer.swapBuffers();
			
			endTime = System.currentTimeMillis();
			deltaTime = (float)(endTime - startTime) / 1000.0f;
			startTime = endTime;
		}
	}
}
