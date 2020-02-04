package vrpproblems.sintef.solver;

import common.optaplanner.basedomain.DistanceType;
import common.optaplanner.basedomain.Job;
import common.optaplanner.basedomain.Standstill;
import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

public class SintefJobNearbyDistanceMeter implements NearbyDistanceMeter<Job, Standstill> {

  @Override
  public double getNearbyDistance(Job origin, Standstill destination) {
    return origin.getTravelTimeInSecondsTo(DistanceType.STRAIGHT_LINE_DISTANCE, destination);
  }
}
