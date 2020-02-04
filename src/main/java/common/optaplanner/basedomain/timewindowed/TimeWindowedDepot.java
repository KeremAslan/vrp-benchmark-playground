package common.optaplanner.basedomain.timewindowed;

import common.optaplanner.basedomain.Depot;
import common.optaplanner.basedomain.Location;
import org.threeten.extra.Interval;

import java.time.ZoneId;


public abstract class TimeWindowedDepot extends Depot {

  private Interval operationalTimeWindow;
  private ZoneId zoneId;

  public TimeWindowedDepot(Location location, String id, Interval operationalTimeWindow, ZoneId zoneId) {
    super(location, id);
    this.operationalTimeWindow = operationalTimeWindow;
    this.zoneId = zoneId;
  }


  public ZoneId getZoneId() {
    return zoneId;
  }

  public Interval getOperationalTimeWindow() {
    return operationalTimeWindow;
  }
}
