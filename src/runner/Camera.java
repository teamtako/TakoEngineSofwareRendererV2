package runner;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import math.Matrix4;
import math.Vector3;

public class Camera implements KeyListener {
//Matrix 4 view
//Matrix 4 projection
//Vector3 Position
//Vector 3 up
//Vector 3 right
// Vector 3 forward
//Quaternion orientation

Matrix4 view;

Vector3 position;
Vector3 up;
Vector3 right;
Vector3 forward;
public Matrix4 projection;

public Camera(){
	view = new Matrix4();
	projection = new Matrix4();
	position = new Vector3(0, 0, 0);
	up = new Vector3(0,1,0);
	right = new Vector3(1,0,0);
	forward = new Vector3(0,0,1);
	
}


public void setPerspectiveProjection(float fov, float aspect, float near, float far) {
	projection.m[0][0] = (float) (1f/(aspect*Math.tan(fov/2f)));
	projection.m[1][1] = (float) (1f/(aspect*Math.tan(fov/2f)));
	projection.m[2][2] = (float) (-near-far)/(near-far);
	projection.m[3][2] = (float) (2f*far*near)/(near-far);
	projection.m[2][3] = 1;
}

public void setOrthographicProjection(float right, float left, float top, float bottom, float far, float near) {
	projection.m[0][0] = (right-left)/2f;
	projection.m[1][1] = (top-bottom)/2f;
	projection.m[2][2] = (far-near)/-2f;
	projection.m[3][0] = (left+right)/2f;
	projection.m[3][1] = (top+bottom)/2f;
	projection.m[3][2] = -(far+near)/2f;
	projection.m[3][3] = 1;
}

void updateView() {
	
}


@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}


@Override
public void keyPressed(KeyEvent e) {
	// TODO Auto-generated method stub
	
}


@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

}
