package vrpproblems.sintef.solver;

import common.basedomain.DistanceType;
import common.basedomain.Job;
import common.basedomain.Standstill;
import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

public class SintefJobNearbyDistanceMeter implements NearbyDistanceMeter<Job, Standstill> {

  @Override
  public double getNearbyDistance(Job origin, Standstill destination) {
    return origin.getTravelTimeInSecondsTo(DistanceType.STRAIGHT_LINE_DISTANCE, destination);
  }
}
