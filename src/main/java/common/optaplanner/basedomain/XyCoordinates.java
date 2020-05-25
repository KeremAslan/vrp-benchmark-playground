package common.optaplanner.basedomain;

import common.optaplanner.basedomain.BaseCoordinate;

public class XyCoordinates implements BaseCoordinate {

    private Double x;
    private Double y;

    public XyCoordinates(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public XyCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Double getLatitude() {
        return x;
    }

    @Override
    public Double getLongitude() {
        return y;
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
