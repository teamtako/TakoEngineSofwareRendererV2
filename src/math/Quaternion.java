package math;

public class Quaternion {
    public float x;
    public float y;
    public float z;
    public float w;

    public Quaternion(){
        this(0, 0, 0, 1);
    }

    public Quaternion(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Quaternion rotationToQuaternion(Vector3 axis, float angle){
        float hang = angle / 2.0f;
        float sinHang = (float)Math.sin(hang);
        Quaternion q = new Quaternion(axis.x * sinHang, axis.y * sinHang, axis.z * sinHang, (float)Math.cos(hang));
        q.normalize();
        return q;
    }

    public static Quaternion copy(Quaternion q){
        return new Quaternion(q.x, q.y, q.z, q.w);
    }

    public float length(){
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }

    public void normalize(){
        float l = this.length();
        if(l != 0){
            this.x /= l;
            this.y /= l;
            this.z /= l;
            this.w /= l;
        }else{
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.w = 0;
        }
    }

    public void multiply(Quaternion q2){
        this.x =   this.x * q2.w + this.y * q2.z - this.z * q2.y + this.w * q2.x;
        this.y =  -this.x * q2.z + this.y * q2.w + this.z * q2.x + this.w * q2.y;
        this.z =   this.x * q2.y - this.y * q2.x + this.z * q2.w + this.w * q2.z;
        this.w =  -this.x * q2.x - this.y * q2.y - this.z * q2.z + this.w * q2.w;
    }

    public Matrix4 toMatrix4(){
        this.normalize();
        Matrix4 m = new Matrix4();
        m.m[0][0] = (1f - (2f * (this.y * this.y)) - (2f * (this.z * this.z)));
        m.m[0][1] = ((2f * this.x * this.y) + (2f * this.z * this.w));
        m.m[0][2] = ((2f * this.x * this.z) - (2f * this.y * this.w));
        m.m[0][3] = 0f;
        m.m[1][0] = ((2f * this.x * this.y) - (2f * this.z * this.w));
        m.m[1][1] = (1f - (2f * this.x * this.x) - (2f * (this.z *  this.z)));
        m.m[1][2] = (2f * (this.y * this.z) + 2f * (this.x * this.w));
        m.m[1][3] = 0f;
        m.m[2][0] = ((2f * this.x * this.z) + (2f * this.y * this.w));
        m.m[2][1] = ((2f * this.y  * this.z) - (2f * this.x * this.w));
        m.m[2][2] = (1f - (2 * this.x * this.x) - (2 * (this.y *  this.y)));
        m.m[2][3] = 0f; m.m[3][0] = 0f; m.m[3][1] = 0f; m.m[3][2] = 0f; m.m[3][3] = 1f;
        return m;
    }

    public void rotate(Vector3 angle, float degrees){
        Quaternion q = Quaternion.rotationToQuaternion(angle, degrees);
        this.multiply(q);
    }
}