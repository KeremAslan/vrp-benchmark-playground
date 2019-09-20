package common.basedomain;


public abstract class Location {

  private String id;
  private BaseCoordinate coordinates;

  public Location(String id, BaseCoordinate coordinates) {
    this.id = id;
    this.coordinates = coordinates;
  }

  public String getId() {
    return id;
  }

  public BaseCoordinate getCoordinates() {
    return coordinates;
  }

  public abstract long getDistanceTo(DistanceType distanceType, Location location);

  @Override
  public String toString() {
    return "Location{" +
        "id='" + id + '\'' +
        ", coordinates=" + coordinates +
        '}';
  }

  //  public long getDistanceTo(DistanceType distanceType, Location location) {
//    if (DistanceType.STRAIGHT_LINE_DISTANCE == distanceType) {
//      return Math.round(
//          Math.sqrt(
//          Math.pow(coordinates.latitude() - location.getCoordinates().latitude(), 2)
//              + Math.pow(coordinates.longitude() - location.getCoordinates().longitude(), 2)));
//    } else {
//      throw new NotImplementedException("Not yet implemented");
//    }
//  }
}
