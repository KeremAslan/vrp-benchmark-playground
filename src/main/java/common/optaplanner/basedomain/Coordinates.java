package common.optaplanner.basedomain;

public class Coordinates implements BaseCoordinate {

    private double latitude;
    private double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public Double latitude() {
        return getLatitude();
    }

    @Override
    public Double longitude() {
        return getLongitude();
    }
}
