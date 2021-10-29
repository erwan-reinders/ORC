package MathClass;

public class Vec2 {
    public double x;
    public double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(double x) {
        this(x, 0.0);
    }

    public Vec2() {
        this(0.0, 0.0);
    }

    public Vec2(Vec2 v) {
        this(v.x, v.y);
    }

    public double length() {
        return Math.sqrt(x*x + y*y);
    }

    public double squareLength() {
        return x*x + y*y;
    }

    public void add(Vec2 v) {
        x += v.x;
        y += v.y;
    }

    public void mult(Vec2 v) {
        x *= v.x;
        y *= v.y;
    }

    public double dot(Vec2 v) {
        return x * v.x + y * v.y;
    }

    public void normalize() {
        double len = length();
        x /= len;
        y /= len;
    }
}
