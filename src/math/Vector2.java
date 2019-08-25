package math;

public class Vector2 {
	public float x;
	public float y;
	
	public Vector2(){
		this(0, 0);
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void add(Vector2 v){
		x += v.x;
		y += v.y;
	}

	public void sub(Vector2 v){
		x -= v.x;
		y -= v.y;
	}

	public static float length(Vector2 v1, Vector2 v2){
		float d1 = v2.x - v1.x;
		float d2 = v2.y - v1.y;
		return (float)Math.sqrt(d1 * d1 + d2 * d2);
	}
}
