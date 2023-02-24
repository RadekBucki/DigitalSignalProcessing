package backend.radar.model;

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

    public double calculateRealDistance(double xFrom, double yFrom) {
        return Math.sqrt((X - xFrom) * (X - xFrom) + (Y - yFrom) * (Y - yFrom));
    }

    public void move(double timeDiff) {
        X += speedX * timeDiff;
        Y += speedY * timeDiff;
    }
}
