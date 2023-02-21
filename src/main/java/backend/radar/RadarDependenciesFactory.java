package backend.radar;

public class RadarDependenciesFactory {
    public MeasuredObject createMeasuredObject(double x, double y, double speedX, double speedY) {
        return new MeasuredObject(x, y, speedX, speedY);
    }
}
