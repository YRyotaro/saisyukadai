public class Sword {
    private double length;
    private double angle;

    public Sword(double length) {
        this.length = length;
        this.angle = 0.0; // 初期角度は0度
    }

    //角度の保持
    public void setAngle(double angle) {
        this.angle = angle;
    }

    //角度と長さを極座標のように計算しx座標y座標を得る
    public double getX() {
        return length * Math.cos(angle);
    }

    public double getY() {
        return length * Math.sin(angle);
    }
}
