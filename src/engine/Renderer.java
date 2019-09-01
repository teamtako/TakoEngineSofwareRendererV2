package engine;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import math.Matrix4;
import math.Vector2;
import math.Vector3;
import math.Vector4;
import runner.Camera;

public class Renderer{
	public static final int DRAW_TYPE_POINTS = 1;
	public static final int DRAW_TYPE_LINES = 2;
	public static final int DRAW_TYPE_TRIANGLES = 3;
	public static final int BYTES_PER_PIXEL = 4;

	private int width;
	private int height;

	private int bytesPerRow;
	private int totalImageSize;

	private int pointSize = 1;
	private int halfPointSize = 0;

	private int lineSize = 1;
	private int halfLineSize = 0;

	private float[] clearColor = { 0, 0, 0, 1 };
	private byte[] backBuffer;

	private BufferedImage frameBuffer;

	JFrame window;
	JLabel display;

	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;

		window = new JFrame();
		display = new JLabel();
		frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

		window.add(display);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		bytesPerRow = width * BYTES_PER_PIXEL;
		totalImageSize = width * height * BYTES_PER_PIXEL;
		backBuffer = new byte[totalImageSize];
	}
	
	public void renderVertecies(ArrayList<Vector3> arr) {
		for(int i = 0;i<arr.size();i+=3) {
			renderTriangle2D(new Vector2(arr.get(i).x,arr.get(i).y),new Vector2(arr.get(i+1).x,arr.get(i+1).y),new Vector2(arr.get(i+2).x,arr.get(i+2).y),new Vector4(1f, 0, 0, 1f), new Vector4(0, 1f, 0, 1f), new Vector4(0, 0, 1f, 1f));
		}
	}

	public void swapBuffers() {
		byte[] imgData = ((DataBufferByte) frameBuffer.getRaster().getDataBuffer()).getData();
		System.arraycopy(backBuffer, 0, imgData, 0, backBuffer.length);
		display.setIcon(new ImageIcon(frameBuffer));
		window.pack();
	}

	public void setClearColor(float r, float g, float b, float a) {
		clearColor[0] = r;
		clearColor[1] = g;
		clearColor[2] = b;
		clearColor[3] = a;
	}

	public void clearBackBuffer() {
		for (int i = 0; i < totalImageSize; i += 4) {
			backBuffer[i] = (byte) (clearColor[3] * 255.0);
			backBuffer[i + 1] = (byte) (clearColor[2] * 255.0);
			backBuffer[i + 2] = (byte) (clearColor[1] * 255.0);
			backBuffer[i + 3] = (byte) (clearColor[0] * 255.0);
		}
	}

	public void setPointSize(int sz) {
		if (sz < 0)
			sz = 0;

		pointSize = sz;
		halfPointSize = sz / 2;
	}

	public void setLineSize(int sz) {
		if (sz < 0)
			sz = 0;

		lineSize = sz;
		halfLineSize = sz / 2;
	}

	public void renderPixel(int x, int y) {
		y = height - y;
		Vector4 color = new Vector4(1, 1, 1, 1);
		if (x < 0 || y < 0 || x >= width || y >= height)
			return;
		int index = (y * bytesPerRow) + (x * BYTES_PER_PIXEL);
		backBuffer[index] = (byte) (color.w * 255);
		backBuffer[index + 1] = (byte) (color.z * 255);
		backBuffer[index + 2] = (byte) (color.y * 255);
		backBuffer[index + 3] = (byte) (color.x * 255);
	}

	public void renderPixel(int x, int y, Vector4 color) {
		y = height - y;
		if (x < 0 || y < 0 || x >= width || y >= height)
			return;
		int index = (y * bytesPerRow) + (x * BYTES_PER_PIXEL);
		backBuffer[index] = (byte) (color.w * 255);
		backBuffer[index + 1] = (byte) (color.z * 255);
		backBuffer[index + 2] = (byte) (color.y * 255);
		backBuffer[index + 3] = (byte) (color.x * 255);
	}

	public void renderPoint2D(Vector2 v) {
		int x = (int) (((v.x + 1.0) * 0.5) * width);
		int y = (int) (((v.y + 1.0) * 0.5) * height);
		x -= halfPointSize;
		y -= halfPointSize;
		for (int j = 0; j < pointSize; j++) {
			for (int k = 0; k < pointSize; k++) {
				renderPixel(x + j, y + k);
			}
		}
	}

	public void renderPoint2D(Vector2 v, Vector4 color){
		int x = (int) (((v.x + 1.0) * 0.5) * width);
		int y = (int) (((v.y + 1.0) * 0.5) * height);
		x -= halfPointSize;
		y -= halfPointSize;
		for (int j = 0; j < pointSize; j++) {
			for (int k = 0; k < pointSize; k++) {
				renderPixel(x + j, y + k, color);
			}
		}
	}

	public void renderLine2D(Vector2 v1, Vector2 v2) {
		int x1 = (int) (((v1.x + 1.0) * 0.5) * width);
		int y1 = (int) (((v1.y + 1.0) * 0.5) * height);
		int x2 = (int) (((v2.x + 1.0) * 0.5) * width);
		int y2 = (int) (((v2.y + 1.0) * 0.5) * height);

		int rise = y2 - y1;
		int run = x2 - x1;
		
		if (run != 0) {
			float m = (float) rise / (float) run;
			float b = (float) y1 - (m * (float) x1);
			int ars = Math.abs(rise);
			int arn = Math.abs(run);
			if (ars > arn) {
				int addAmt = (rise > 0 ? 1 : -1);
				for (int i = 0; i < ars; i++) {
					int y = y1 + (i * addAmt);
					int x = (int) (((float) y / m) - (b / m));
					x -= halfLineSize;
					for (int j = 0; j < lineSize; j++) {
						renderPixel(x++, y);
					}
				}
			} else {
				int addAmt = (run > 0 ? 1 : -1);
				for (int i = 0; i < arn; i++) {
					int x = x1 + (i * addAmt);
					int y = (int) ((m * (float) x) + b);
					y -= halfLineSize;
					for (int j = 0; j < lineSize; j++) {
						renderPixel(x, y++);
					}
				}
			}
		} else {
			int ars = Math.abs(rise);
			int addAmt = (rise > 0 ? 1 : -1);
			for (int i = 0; i < ars; i++) {
				int y = y1 + (i * addAmt);
				int x = x1;
				x -= halfLineSize;
				for (int j = 0; j < lineSize; j++) {
					renderPixel(x++, y);
				}
			}
		}
	}

	public void renderLine2D(Vector2 v1, Vector2 v2, Vector4 color1, Vector4 color2) {
		int x1 = (int) (((v1.x + 1.0) * 0.5) * width);
		int y1 = (int) (((v1.y + 1.0) * 0.5) * height);
		int x2 = (int) (((v2.x + 1.0) * 0.5) * width);
		int y2 = (int) (((v2.y + 1.0) * 0.5) * height);

		Vector2 p1 = new Vector2(x1, y1);
		Vector2 p2 = new Vector2(x2, y2);

		int rise = y2 - y1;
		int run = x2 - x1;
		
		if (run != 0) {
			float m = (float) rise / (float) run;
			float b = (float) y1 - (m * (float) x1);
			int ars = Math.abs(rise);
			int arn = Math.abs(run);
			if (ars > arn) {
				int addAmt = (rise > 0 ? 1 : -1);
				for (int i = 0; i < ars; i++) {
					int y = y1 + (i * addAmt);
					int x = (int) (((float) y / m) - (b / m));
					x -= halfLineSize;
					for (int j = 0; j < lineSize; j++) {
						Vector2 pt = new Vector2(x, y);
						Vector4 c = calculateColor(p1, p2, pt, color1, color2);
						renderPixel(x++, y, c);
					}
				}
			} else {
				int addAmt = (run > 0 ? 1 : -1);
				for (int i = 0; i < arn; i++) {
					int x = x1 + (i * addAmt);
					int y = (int) ((m * (float) x) + b);
					y -= halfLineSize;
					for (int j = 0; j < lineSize; j++) {
						Vector2 pt = new Vector2(x, y);
						Vector4 c = calculateColor(p1, p2, pt, color1, color2);
						renderPixel(x, y++, c);
					}
				}
			}
		} else {
			int ars = Math.abs(rise);
			int addAmt = (rise > 0 ? 1 : -1);
			for (int i = 0; i < ars; i++) {
				int y = y1 + (i * addAmt);
				int x = x1;
				x -= halfLineSize;
				for (int j = 0; j < lineSize; j++) {
					Vector2 pt = new Vector2(x, y);
					Vector4 c = calculateColor(p1, p2, pt, color1, color2);
					renderPixel(x++, y, c);
				}
			}
		}
	}

	private void renderFlatTopTriangle(Vector2 bottom, Vector2 topLeft, Vector2 topRight){
		int rise = (int)(topLeft.y - bottom.y);
		int run1 = (int)(topLeft.x - bottom.x);
		int run2 = (int)(topRight.x - bottom.x);

		int[] startXs = new int[rise];
		int[] endXs = new int[rise];

		if(run1 != 0){
			float m = (float)rise / (float)run1;
			float b = bottom.y - (m * bottom.x);
			for(int i = 0; i < rise; i++){
				startXs[i] = (int)(((float)(bottom.y + i) / m) - (b / m));
			}
		}else{
			for(int i = 0; i < rise; i++){
				startXs[i] = (int)bottom.x;
			}
		}

		if(run2 != 0){
			float m = (float)rise / (float)run2;
			float b = bottom.y - (m * bottom.x);
			for(int i = 0; i < rise; i++){
				endXs[i] = (int)(((float)(bottom.y + i) / m) - (b / m));
			}
		}else{
			for(int i = 0; i < rise; i++){
				endXs[i] = (int)bottom.x;
			}	
		}
		System.out.println("check");
		System.out.println("check");
		int ctr = 0;
		for(int y = (int)bottom.y; y < topLeft.y; y++){
			for(int x = (int)startXs[ctr]; x < endXs[ctr]; x++){
				renderPixel(x, y);
			}
			ctr++;
		}
	}

	private void renderFlatBottomTriangle(Vector2 top, Vector2 bottomLeft, Vector2 bottomRight){
		int rise = (int)(top.y - bottomLeft.y);
		int run1 = (int)(top.x - bottomLeft.x);
		int run2 = (int)(top.x - bottomRight.x);

		int[] startXs = new int[rise];
		int[] endXs = new int[rise];

		if(run1 != 0){
			float m = (float)rise / (float)run1;
			float b = top.y - (m * top.x);
			for(int i = 0; i < rise; i++){
				startXs[i] = (int)(((float)(bottomLeft.y + i) / m) - (b / m));
			}
		}else{
			for(int i = 0; i < rise; i++){
				startXs[i] = (int)top.x;
			}
		}

		if(run2 != 0){
			float m = (float)rise / (float)run2;
			float b = top.y - (m * top.x);
			for(int i = 0; i < rise; i++){
				endXs[i] = (int)(((float)(bottomRight.y + i) / m) - (b / m));
			}
		}else{
			for(int i = 0; i < rise; i++){
				endXs[i] = (int)top.x;
			}
		}

		int ctr = 0;
		for(int y = (int)bottomLeft.y; y < top.y; y++){
			for(int x = (int)startXs[ctr]; x < endXs[ctr]; x++){
				renderPixel(x, y);
			}
			ctr++;
		}
	}

	private void renderFlatTopTriangle(Vector2 bottom, Vector2 topLeft, Vector2 topRight,  Vector4 c1, Vector4 c2, Vector4 c3){
		int rise = (int)(topLeft.y - bottom.y);
		int run1 = (int)(topLeft.x - bottom.x);
		int run2 = (int)(topRight.x - bottom.x);

		int[] startXs = new int[rise];
		int[] endXs = new int[rise];

		if(run1 != 0){
			float m = (float)rise / (float)run1;
			float b = bottom.y - (m * bottom.x);
			for(int i = 0; i < rise; i++){
				startXs[i] = (int)(((float)(bottom.y + i) / m) - (b / m));
			}
		}else{
			for(int i = 0; i < rise; i++){
				startXs[i] = (int)bottom.x;
			}
		}

		if(run2 != 0){
			float m = (float)rise / (float)run2;
			float b = bottom.y - (m * bottom.x);
			for(int i = 0; i < rise; i++){
				endXs[i] = (int)(((float)(bottom.y + i) / m) - (b / m));
			}
		}else{
			for(int i = 0; i < rise; i++){
				endXs[i] = (int)bottom.x;
			}	
		}

		int ctr = 0;
		for(int y = (int)bottom.y; y < topLeft.y; y++){
			for(int x = (int)startXs[ctr]; x < endXs[ctr]; x++){
				Vector4 c = calculateColor(bottom, topLeft, topRight, new Vector2(x, y), c1, c2, c3);
				renderPixel(x, y, c);
			}
			ctr++;
		}
	}

	private void renderFlatBottomTriangle(Vector2 top, Vector2 bottomLeft, Vector2 bottomRight, Vector4 c1, Vector4 c2, Vector4 c3){
		int rise = (int)(top.y - bottomLeft.y);
		int run1 = (int)(top.x - bottomLeft.x);
		int run2 = (int)(top.x - bottomRight.x);

		int[] startXs = new int[rise];
		int[] endXs = new int[rise];

		if(run1 != 0){
			float m = (float)rise / (float)run1;
			float b = top.y - (m * top.x);
			for(int i = 0; i < rise; i++){
				startXs[i] = (int)(((float)(bottomLeft.y + i) / m) - (b / m));
			}
		}else{
			for(int i = 0; i < rise; i++){
				startXs[i] = (int)top.x;
			}
		}

		if(run2 != 0){
			float m = (float)rise / (float)run2;
			float b = top.y - (m * top.x);
			for(int i = 0; i < rise; i++){
				endXs[i] = (int)(((float)(bottomRight.y + i) / m) - (b / m));
			}
		}else{
			for(int i = 0; i < rise; i++){
				endXs[i] = (int)top.x;
			}
		}

		int ctr = 0;
		for(int y = (int)bottomLeft.y; y < top.y; y++){
			for(int x = (int)startXs[ctr]; x < endXs[ctr]; x++){
				Vector4 c = calculateColor(top, bottomLeft, bottomRight, new Vector2(x, y), c1, c2, c3);
				renderPixel(x, y, c);
			}
			ctr++;
		}
	}

	private Vector4 calculateColor(Vector2 p1, Vector2 p2, Vector2 tp, Vector4 color1, Vector4 color2){
		float lineLen = Vector2.length(p1, p2);
		float pLen = Vector2.length(p1, tp);
		if(lineLen != 0){
			float ratio1 = 1.0f - (pLen / lineLen);
			float ratio2 = 1.0f - ratio1;
			return new Vector4(color1.x * ratio1 + color2.x * ratio2, 
							color1.y * ratio1 + color2.y * ratio2, 
							color1.z * ratio1 + color2.z * ratio2, 
							color1.w * ratio1 + color2.w * ratio2);
		}else{
			return new Vector4((color1.x + color2.x) / 2,
			                   (color1.y + color2.y) / 2,
							   (color1.z + color2.z) / 2,
							   (color1.w + color2.w) / 2);
		}
	}

	private Vector4 calculateColor(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 tp, Vector4 color1, Vector4 color2, Vector4 color3){
		float denom = (p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y);
		float num1 = (p2.y - p3.y) * (tp.x - p3.x) + (p3.x - p2.x) * (tp.y - p3.y);
		float num2 = (p3.y - p1.y) * (tp.x - p3.x) + (p1.x - p3.x) * (tp.y - p3.y);
		float w1 = num1 / denom;
		float w2 = num2 / denom;
		float w3 = 1.0f - w1 - w2;
		return new Vector4(color1.x * w1 + color2.x * w2 + color3.x * w3,
		                   color1.y * w1 + color2.y * w2 + color3.y * w3,
						   color1.z * w1 + color2.z * w2 + color3.z * w3,
						   color1.w * w1 + color2.w * w2 + color3.w * w3);
	}

	public void renderTriangle2D(Vector2 vec1, Vector2 vec2, Vector2 vec3) {
		Vector2 v1 = new Vector2((int)((vec1.x + 1.0f) * 0.5f * width),  (int)((vec1.y + 1.0f) * 0.5f * height));
		Vector2 v2 = new Vector2((int)((vec2.x + 1.0f) * 0.5f * width),  (int)((vec2.y + 1.0f) * 0.5f * height));
		Vector2 v3 = new Vector2((int)((vec3.x + 1.0f) * 0.5f * width),  (int)((vec3.y + 1.0f) * 0.5f * height));
		if(v1.y == v2.y){
			if(v3.y > v1.y){
				if(v1.x < v2.x){
					renderFlatBottomTriangle(v3, v1, v2);
				}else{
					renderFlatBottomTriangle(v3, v2, v1);
				}
			}else{
				if(v1.x < v2.x){
					renderFlatTopTriangle(v3, v1, v2);
				}else{
					renderFlatTopTriangle(v3, v2, v1);
				}
			}
		}else if(v2.y == v3.y){
			if(v1.y > v2.y){
				if(v2.x < v3.x){
					renderFlatBottomTriangle(v1, v2, v3);
				}else{
					renderFlatBottomTriangle(v1, v3, v2);
				}
			}else{
				if(v2.x < v3.x){
					renderFlatTopTriangle(v1, v2, v3);
				}else{
					renderFlatTopTriangle(v1, v3, v2);
				}
			}
		}else if(v1.y == v3.y){
			if(v2.y > v1.y){
				if(v1.x < v3.x){
					renderFlatBottomTriangle(v2, v1, v3);
				}else{
					renderFlatBottomTriangle(v2, v3, v1);
				}
			}else{
				if(v1.x < v3.x){
					renderFlatTopTriangle(v2, v1, v3);
				}else{
					renderFlatTopTriangle(v2, v3, v1);
				}
			}
		}else{
			Vector2 topVec;
			Vector2 midVec;
			Vector2 lowVec;
			Vector2 cutVec;
			
			if(v1.y < v2.y && v1.y < v3.y){
				lowVec = v1;
				if(v2.y < v3.y){
					midVec = v2;
					topVec = v3;
				}else{
					midVec = v3;
					topVec = v2;
				}
			}else if(v2.y < v3.y && v2.y < v1.y){
				lowVec = v2;
				if(v1.y < v3.y){
					midVec = v1;
					topVec = v3;
				}else{
					midVec = v3;
					topVec = v1;
				}
			}else{
				lowVec = v3;
				if(v2.y < v1.y){
					midVec = v2;
					topVec = v1;
				}else{
					midVec = v1;
					topVec = v2;
				}
			}
			int rise = (int)(topVec.y - lowVec.y);
			int run = (int)(topVec.x - lowVec.x);
			if(run != 0){
				float m = (float)rise / (float)run;
				float b = topVec.y - (m * topVec.x);
				int x = (int)((midVec.y / m) - (b / m));
				cutVec = new Vector2(x, midVec.y);
			}else{
				cutVec = new Vector2(topVec.x, midVec.y);
			}
			
			if(cutVec.x < midVec.x){
				renderFlatBottomTriangle(topVec, cutVec, midVec);
				renderFlatTopTriangle(lowVec, cutVec, midVec);
			}else{
				renderFlatBottomTriangle(topVec, midVec, cutVec);
				renderFlatTopTriangle(lowVec, midVec, cutVec);
			}
		}
	}

	public void renderTriangle2D(Vector2 vec1, Vector2 vec2, Vector2 vec3, Vector4 c1, Vector4 c2, Vector4 c3) {
		Vector2 v1 = new Vector2((int)((vec1.x + 1.0f) * 0.5f * width),  (int)((vec1.y + 1.0f) * 0.5f * height));
		Vector2 v2 = new Vector2((int)((vec2.x + 1.0f) * 0.5f * width),  (int)((vec2.y + 1.0f) * 0.5f * height));
		Vector2 v3 = new Vector2((int)((vec3.x + 1.0f) * 0.5f * width),  (int)((vec3.y + 1.0f) * 0.5f * height));
		if(v1.y == v2.y){
			if(v3.y > v1.y){
				if(v1.x < v2.x){
					renderFlatBottomTriangle(v3, v1, v2, c3, c1, c2);
				}else{
					renderFlatBottomTriangle(v3, v2, v1, c3, c2, c1);
				}
			}else{
				if(v1.x < v2.x){
					renderFlatTopTriangle(v3, v1, v2, c3, c1, c2);
				}else{
					renderFlatTopTriangle(v3, v2, v1, c3, c2, c1);
				}
			}
		}else if(v2.y == v3.y){
			if(v1.y > v2.y){
				if(v2.x < v3.x){
					renderFlatBottomTriangle(v1, v2, v3, c1, c2, c3);
				}else{
					renderFlatBottomTriangle(v1, v3, v2, c1, c3, c2);
				}
			}else{
				if(v2.x < v3.x){
					renderFlatTopTriangle(v1, v2, v3, c1, c2, c3);
				}else{
					renderFlatTopTriangle(v1, v3, v2, c1, c3, c2);
				}
			}
		}else if(v1.y == v3.y){
			if(v2.y > v1.y){
				if(v1.x < v3.x){
					renderFlatBottomTriangle(v2, v1, v3, c2, c1, c3);
				}else{
					renderFlatBottomTriangle(v2, v3, v1, c2, c3, c1);
				}
			}else{
				if(v1.x < v3.x){
					renderFlatTopTriangle(v2, v1, v3, c2, c1, c3);
				}else{
					renderFlatTopTriangle(v2, v3, v1, c2, c3, c1);
				}
			}
		}else{
			Vector2 topVec;
			Vector2 midVec;
			Vector2 lowVec;
			Vector2 cutVec;

			Vector4 topColor;
			Vector4 midColor;
			Vector4 lowColor;
			Vector4 cutColor;
			
			if(v1.y < v2.y && v1.y < v3.y){
				lowVec = v1;
				lowColor = c1;
				if(v2.y < v3.y){
					midVec = v2;
					topVec = v3;
					midColor = c2;
					topColor = c3;
				}else{
					midVec = v3;
					topVec = v2;
					midColor = c3;
					topColor = c2;
				}
			}else if(v2.y < v3.y && v2.y < v1.y){
				lowVec = v2;
				lowColor = c2;
				if(v1.y < v3.y){
					midVec = v1;
					topVec = v3;
					midColor = c1;
					topColor = c3;
				}else{
					midVec = v3;
					topVec = v1;
					midColor = c3;
					topColor = c1;
				}
			}else{
				lowVec = v3;
				lowColor = c3;
				if(v2.y < v1.y){
					midVec = v2;
					topVec = v1;
					midColor = c2;
					topColor = c1;
				}else{
					midVec = v1;
					topVec = v2;
					midColor = c1;
					topColor = c2;
				}
			}
			int rise = (int)(topVec.y - lowVec.y);
			int run = (int)(topVec.x - lowVec.x);
			if(run != 0){
				float m = (float)rise / (float)run;
				float b = topVec.y - (m * topVec.x);
				int x = (int)((midVec.y / m) - (b / m));
				cutVec = new Vector2(x, midVec.y);
			}else{
				cutVec = new Vector2(topVec.x, midVec.y);
			}

			cutColor = calculateColor(topVec, lowVec, cutVec, topColor, lowColor);
			
			if(cutVec.x < midVec.x){
				renderFlatBottomTriangle(topVec, cutVec, midVec, topColor, cutColor, midColor);
				renderFlatTopTriangle(lowVec, cutVec, midVec, lowColor, cutColor, midColor);
			}else{
				renderFlatBottomTriangle(topVec, midVec, cutVec, topColor, midColor, cutColor);
				renderFlatTopTriangle(lowVec, midVec, cutVec, lowColor, midColor, cutColor);
			}
		}
	}

	public void renderTriangle3D(Vector3 p1, Vector3 p2, Vector3 p3, Camera cam){
		Vector4 p1v4 = new Vector4(p1.x, p1.y, p1.z, 1.0f);
		Vector4 p2v4 = new Vector4(p2.x, p2.y, p2.z, 1.0f);
		Vector4 p3v4 = new Vector4(p3.x, p3.y, p3.z, 1.0f);

		Matrix4 projView = Matrix4.multiply(cam.projectionMatrix, cam.viewMatrix); 

		Vector4 p1a = Matrix4.multiply(projView, p1v4);
		Vector4 p2a = Matrix4.multiply(projView, p2v4);
		Vector4 p3a = Matrix4.multiply(projView, p3v4);

		if(p1a.z > 0.0001f && p1a.z < 1000.0f &&
		   p2a.z > 0.0001f && p2a.z < 1000.0f &&
		   p3a.z > 0.0001f && p3a.z < 1000.0f){
			renderTriangle2D(new Vector2(p1a.x / p1a.z, p1a.y / p1a.z), new Vector2(p2a.x / p2a.z, p2a.y / p2a.z), new Vector2(p3a.x / p3a.z, p3a.y / p3a.z));
		}
	}
	
	public void renderTriangle3D(Vector3 p1, Vector3 p2, Vector3 p3, Vector4 c1, Vector4 c2, Vector4 c3, Camera cam){
		Vector4 p1v4 = new Vector4(p1.x, p1.y, p1.z, 1.0f);
		Vector4 p2v4 = new Vector4(p2.x, p2.y, p2.z, 1.0f);
		Vector4 p3v4 = new Vector4(p3.x, p3.y, p3.z, 1.0f);

		Matrix4 projView = Matrix4.multiply(cam.projectionMatrix, cam.viewMatrix); 

		Vector4 p1a = Matrix4.multiply(projView, p1v4);
		Vector4 p2a = Matrix4.multiply(projView, p2v4);
		Vector4 p3a = Matrix4.multiply(projView, p3v4);

		if(p1a.z > 0.0001f && p1a.z < 1000.0f &&
		   p2a.z > 0.0001f && p2a.z < 1000.0f &&
		   p3a.z > 0.0001f && p3a.z < 1000.0f){
			renderTriangle2D(new Vector2(p1a.x / p1a.z, p1a.y / p1a.z), 
			                 new Vector2(p2a.x / p2a.z, p2a.y / p2a.z), 
							 new Vector2(p3a.x / p3a.z, p3a.y / p3a.z), 
							 c1, c2, c3);
		}
	}
	
}
