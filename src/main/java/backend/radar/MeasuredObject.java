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

    public double calculateRealDistance(double radarX, double radarY) {
        return Math.sqrt((X - radarX) * (X - radarX) + (Y - radarY) * (Y - radarY));
    }

    public void move(double timeDiff) {
        X += speedX * timeDiff;
        Y += speedY * timeDiff;
    }
}
