package vrpproblems.sintef.domain;

import common.basedomain.BaseCoordinate;
import common.basedomain.DistanceType;
import common.basedomain.Location;
import org.apache.commons.lang3.NotImplementedException;


public class SintefLocation extends Location {

  public SintefLocation(String id, BaseCoordinate coordinates) {
    super(id, coordinates);
  }

    public long getDistanceTo(DistanceType distanceType, Location location) {
    if (DistanceType.STRAIGHT_LINE_DISTANCE == distanceType) {
      return Math.round(
          Math.sqrt(
          Math.pow(getCoordinates().latitude() - location.getCoordinates().latitude(), 2)
              + Math.pow(getCoordinates().longitude() - location.getCoordinates().longitude(), 2)));
    } else {
      throw new NotImplementedException("Not yet implemented");
    }
  }

}
