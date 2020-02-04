package common.optaplanner.basedomain.timewindowed;

import common.optaplanner.basedomain.Depot;
import common.optaplanner.basedomain.Vehicle;
import common.optaplanner.basedomain.VehicleType;
import org.threeten.extra.Interval;

import java.time.Instant;
import java.time.ZoneId;


public abstract class TimeWindowedVehicle extends Vehicle {

  private Interval operationalTimeWindow;

  private ZoneId zoneId;

  public TimeWindowedVehicle(String id, VehicleType vehicleType, Depot startDepot, Depot endDepot,
                             Interval operationalTimeWindow, ZoneId zoneId) {
    super(id, vehicleType, startDepot, endDepot);
    this.operationalTimeWindow = operationalTimeWindow;
    this.zoneId = zoneId;
  }

  public Interval getOperationalTimeWindow() {
    return operationalTimeWindow;
  }

  public ZoneId getZoneId() {
    return zoneId;
  }

  /**
   * Returns true if pointInTime is contained by the operationalTimeWindow
   * @param pointInTime a point in time since epoch expressed in seconds
   * @return
   */
  public boolean contains(Long pointInTime) {
    return operationalTimeWindow.contains(Instant.ofEpochSecond(pointInTime));
  }
}
