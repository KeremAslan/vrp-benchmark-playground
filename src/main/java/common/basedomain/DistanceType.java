package common.basedomain;

public enum DistanceType {

  /** Straight line distance in a 2D plane*/
  STRAIGHT_LINE_DISTANCE,
  /** Straight line distance in a 3D plane*/
  HAVERSINE_DISTANCE,
  /** Actual road distance as per mapping information*/
  ROAD_DISTANCE
}
