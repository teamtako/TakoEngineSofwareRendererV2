package runner;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import math.Matrix4;
import math.Quaternion;
import math.Vector3;

public class Camera implements KeyListener {

	public Matrix4 viewMatrix;
	public Matrix4 projectionMatrix;
	public Quaternion orientation;
	public Vector3 position;
	public Vector3 forwardVec;
	public Vector3 upVec;
	public Vector3 rightVec;

	public boolean forward;
	public boolean back;
	public boolean up;
	public boolean down;
	public boolean left;
	public boolean right;

	public boolean rollLeft;
	public boolean rollRight;
	public boolean pitchUp;
	public boolean pitchDown;
	public boolean yawLeft;
	public boolean yawRight;

	public float moveSpeed = 1;
	public float rotateSpeed = 1;

	public Camera() {
		viewMatrix = new Matrix4(1);
		projectionMatrix = new Matrix4(1);
		position = new Vector3(0, 0, 0);
		orientation = new Quaternion();
		forwardVec = new Vector3(0, 0, 1);
		upVec = new Vector3(0, 1, 0);
		rightVec = new Vector3(1, 0, 0);
	}

	public void setPerspectiveProjection(float fov, float aspect, float near, float far) {
		projectionMatrix = new Matrix4();
		float tanHFov = (float) (Math.tan(fov / 2.0f));
		projectionMatrix.m[0][0] = 1.0f / (aspect * tanHFov);
		projectionMatrix.m[1][1] = 1.0f / tanHFov;
		projectionMatrix.m[2][2] = -(far + near) / (far - near);
		projectionMatrix.m[2][3] = -1.0f;
		projectionMatrix.m[3][2] = -(2.0f * far * near) / (far - near);
	}

	public void updateView(float delta) {
		if (forward) {
			position.add(Vector3.scale(Vector3.scale(forwardVec, moveSpeed), delta));
		}
		if (back) {
			position.sub(Vector3.scale(Vector3.scale(forwardVec, moveSpeed), delta));
		}
		if (left) {
			position.sub(Vector3.scale(Vector3.scale(rightVec, moveSpeed), delta));
		}
		if (right) {
			position.add(Vector3.scale(Vector3.scale(rightVec, moveSpeed), delta));
		}
		if (up) {
			position.add(Vector3.scale(Vector3.scale(upVec, moveSpeed), delta));
		}
		if (down) {
			position.sub(Vector3.scale(Vector3.scale(upVec, moveSpeed), delta));
		}
		if (yawRight) {
			orientation.rotate(upVec, rotateSpeed * delta);
		}
		if (yawLeft) {
			orientation.rotate(upVec, -rotateSpeed * delta);
		}
		if (pitchUp) {
			orientation.rotate(rightVec, -rotateSpeed * delta);
		}
		if (pitchDown) {
			orientation.rotate(rightVec, rotateSpeed * delta);
		}
		if (rollRight) {
			orientation.rotate(forwardVec, -rotateSpeed * delta);
		}
		if (rollLeft) {
			orientation.rotate(forwardVec, rotateSpeed * delta);
		}

		viewMatrix = new Matrix4(1);
		viewMatrix.translate(position);
		viewMatrix = Matrix4.multiply(viewMatrix, orientation.toMatrix4());

		forwardVec = new Vector3(-viewMatrix.m[0][2], -viewMatrix.m[1][2], -viewMatrix.m[2][2]);
		upVec = new Vector3(viewMatrix.m[0][1], viewMatrix.m[1][1], viewMatrix.m[2][1]);
		rightVec = new Vector3(viewMatrix.m[0][0], viewMatrix.m[1][0], viewMatrix.m[2][0]);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		// Transforms
		if (key == KeyEvent.VK_W) {
			forward = true;
		}
		if (key == KeyEvent.VK_S) {
			back = true;
		}
		if (key == KeyEvent.VK_A) {
			left = true;
		}
		if (key == KeyEvent.VK_D) {
			right = true;
		}
		if (key == KeyEvent.VK_Q) {
			up = true;
		}
		if (key == KeyEvent.VK_E) {
			down = true;
		}
		// Rotations
		if (key == KeyEvent.VK_LEFT) {
			yawLeft = true;
		}
		if (key == KeyEvent.VK_RIGHT) {
			yawRight = true;
		}
		if (key == KeyEvent.VK_UP) {
			pitchUp = true;
		}
		if (key == KeyEvent.VK_DOWN) {
			pitchDown = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		// Transforms
		if (key == KeyEvent.VK_W) {
			forward = false;
		}
		if (key == KeyEvent.VK_S) {
			back = false;
		}
		if (key == KeyEvent.VK_A) {
			left = false;
		}
		if (key == KeyEvent.VK_D) {
			right = false;
		}
		if (key == KeyEvent.VK_Q) {
			up = false;
		}
		if (key == KeyEvent.VK_E) {
			down = false;
		}
		// Rotations
		if (key == KeyEvent.VK_LEFT) {
			yawLeft = false;
		}
		if (key == KeyEvent.VK_RIGHT) {
			yawRight = false;
		}
		if (key == KeyEvent.VK_UP) {
			pitchUp = false;
		}
		if (key == KeyEvent.VK_DOWN) {
			pitchDown = false;
		}

	}
}