package vrpproblems.sintef.domain;

import common.optaplanner.basedomain.Depot;
import common.optaplanner.basedomain.DistanceType;
import common.optaplanner.basedomain.Job;
import common.optaplanner.basedomain.Location;
import common.optaplanner.basedomain.Standstill;
import common.optaplanner.basedomain.Vehicle;
import common.optaplanner.basedomain.VehicleType;
import common.optaplanner.basedomain.timewindowed.TimeWindowedVehicle;
import common.optaplanner.persistence.dto.Dto;
import org.apache.commons.lang3.NotImplementedException;
import org.threeten.extra.Interval;

import java.time.ZoneId;


public class SintefVehicle extends TimeWindowedVehicle {

  private int capacity;


  // shadow variable
  /** nextJob is a shadow variable. It refers to the next node in the chain of visits*/
  private Job nextStandstill;

  public SintefVehicle(String id, VehicleType vehicleType, Depot startDepot, Depot endDepot, Interval operationalTimeWindow,
                       int capacity) {
    super(id, vehicleType, startDepot, endDepot, operationalTimeWindow, ZoneId.of("UTC"));
    this.capacity = capacity;
  }

  @Override
  //TechDebt: getStartLocation and getEndLocation would be better
  public Location getLocation() {
    return getStartDepot().getLocation();
  }


  @Override
  public Long getTravelTimeInSecondsTo(DistanceType distanceType, Standstill standstillToTravelTo) {
    return this.getStartDepot().getLocation().getDistanceTo(distanceType, standstillToTravelTo.getLocation());
  }

  @Override
  public Vehicle getVehicle() {
    return this;
  }

  @Override
  public void setNextJob(Job nextStandstill) {
    this.nextStandstill = nextStandstill;
  }

  @Override
  public Job getNextJob() {
    return nextStandstill;
  }

  public int getCapacity() {
    return capacity;
  }

  @Override
  public Dto convertToDto() {
    throw new NotImplementedException("Convert To DTO is not yet implemented for " + this);
  }

  @Override
  public Vehicle safeClone() {
    return new SintefVehicle(getId(), getVehicleType(), getStartDepot(), getEndDepot(), getOperationalTimeWindow(), capacity);
  }


}
