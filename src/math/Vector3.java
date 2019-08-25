package math;

public class Vector3 {
	public float x;
	public float y;
	public float z;
	
	public Vector3(){
		this(0, 0, 0);
	}

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static float length(Vector3 v1, Vector3 v2){
		float d1 = v2.x - v1.x;
		float d2 = v2.y - v1.y;
		float d3 = v2.z - v1.z;
		return (float)Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
	}

	public static Vector3 mul(Vector3 v1, Vector3 v2){
		return new Vector3(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
	}

	public static Vector3 scale(Vector3 v1, float v){
		return new Vector3(v1.x * v, v1.y * v, v1.z * v);
	}

	public void add(Vector3 v){
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public void sub(Vector3 v){
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

}
