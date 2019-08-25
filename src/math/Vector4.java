package math;

public class Vector4 {
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vector4(){
		this(0, 0, 0, 0);
	}

	public Vector4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public static float length(Vector4 v1, Vector4 v2){
		float d1 = v2.x - v1.x;
		float d2 = v2.y - v1.y;
		float d3 = v2.z - v1.z;
		float d4 = v2.w - v1.w;
		return (float)Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3 + d4 * d4);
	}
}
