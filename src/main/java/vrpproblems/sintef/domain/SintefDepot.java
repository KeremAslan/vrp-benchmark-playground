package vrpproblems.sintef.domain;

import common.optaplanner.basedomain.Location;
import common.optaplanner.basedomain.timewindowed.TimeWindowedDepot;
import org.threeten.extra.Interval;

import java.time.ZoneId;


public class SintefDepot extends TimeWindowedDepot {

  public SintefDepot(Location location, String id, Interval timeWindow, ZoneId zoneId) {
    super(location, id, timeWindow, zoneId);
  }

}
