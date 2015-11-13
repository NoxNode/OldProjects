package threeD.math;

public class Vector3F {
	public float x;
	public float y;
	public float z;

	public Vector3F(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3F add(Vector3F v) {
		return new Vector3F(this.x + v.x, this.y + v.y, this.z + v.z);
	}

	public Vector3F sub(Vector3F v) {
		return new Vector3F(this.x - v.x, this.y - v.y, this.z - v.z);
	}

	public Vector3F mul(Vector3F v) {
		return new Vector3F(this.x * v.x, this.y * v.y, this.z * v.z);
	}

	public Vector3F div(Vector3F v) {
		return new Vector3F(this.x / v.x, this.y / v.y, this.z / v.z);
	}
}
