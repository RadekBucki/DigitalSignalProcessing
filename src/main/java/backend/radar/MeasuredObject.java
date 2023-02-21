package backend.radar;

public class MeasuredObject {
    private double X;
    private double Y;
    private final double speedX;
    private final double speedY;

    public MeasuredObject(double x, double y, double speedX, double speedY) {
        X = x;
        Y = y;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public double calculateRealDistance(Radar radar) {
        return Math.sqrt((X - radar.getX()) * (X - radar.getX()) + (Y - radar.getY()) * (Y - radar.getY()));
    }

    public void move(double timeDiff) {
        X += speedX * timeDiff;
        Y += speedY * timeDiff;
    }
}
