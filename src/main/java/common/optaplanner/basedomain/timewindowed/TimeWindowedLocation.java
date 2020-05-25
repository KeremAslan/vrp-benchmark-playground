package common.optaplanner.basedomain.timewindowed;


import common.optaplanner.basedomain.Coordinates;
import common.optaplanner.basedomain.Location;
import org.threeten.extra.Interval;

import java.time.ZoneId;


public abstract class TimeWindowedLocation extends Location {

  private Interval operationalTimeWindow;
  private ZoneId zoneId;

  public TimeWindowedLocation(String id, Coordinates coordinates, Interval operationalTimeWindow,
                              ZoneId zoneId) {

    super(id, coordinates);
    this.zoneId = zoneId;
    this.operationalTimeWindow = operationalTimeWindow;
  }

  public ZoneId getZoneId() {
    return zoneId;
  }

  public Interval getOperationalTimeWindow() {
    return operationalTimeWindow;
  }
}
