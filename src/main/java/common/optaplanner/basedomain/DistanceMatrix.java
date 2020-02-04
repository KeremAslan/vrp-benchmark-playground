package common.optaplanner.basedomain;

public interface DistanceMatrix {

  Integer getTravelTimeInSeconds(Location location1, Location location2);
}
