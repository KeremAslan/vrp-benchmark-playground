package common.basedomain;

public abstract class Depot {

  private Location location;
  private String id;

  public Depot(Location location, String id) {
    this.location = location;
    this.id = id;
  }

  public Location getLocation() {
    return location;
  }

  public String getId() {
    return id;
  }

  public long getDistanceTo(DistanceType distanceType, Location location) {
    return location.getDistanceTo(distanceType, location);
  }
}
