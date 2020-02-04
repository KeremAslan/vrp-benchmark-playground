package vrpproblems.sintef.domain;

import common.optaplanner.basedomain.DistanceType;
import common.optaplanner.basedomain.Job;
import common.optaplanner.basedomain.JobType;
import common.optaplanner.basedomain.Location;
import common.optaplanner.basedomain.Standstill;
import common.optaplanner.basedomain.Vehicle;
import common.optaplanner.basedomain.timewindowed.TimeWindowedJob;
import common.optaplanner.persistence.dto.Dto;
import org.apache.commons.lang3.NotImplementedException;
import org.threeten.extra.Interval;

import java.time.ZoneId;


public class SintefJob extends TimeWindowedJob {

  private int demand;


  /** Default constructor*/
  public SintefJob(String id, JobType jobType, Location location, Interval timeWindow, int demand, long serviceTime, ZoneId zoneId) {
    super(id, jobType, location, timeWindow, serviceTime, zoneId);
    this.demand = demand;
  }

  /* --------------------------- Useful domain methods ------------------------------ */

  @Override
  //TechDebt check if Sintef travel time is in seconds or milliseconds
  public Long getTravelTimeInSecondsFromPreviousStandstill(DistanceType distanceType) {
    return previousStandstill.getLocation().getDistanceTo(distanceType, this.getLocation());
  }


  @Override
  public Long getTravelTimeInSecondsTo(DistanceType distanceType, Standstill standstillToTravelTo) {
    return this.getLocation().getDistanceTo(distanceType, standstillToTravelTo.getLocation());
  }

  public long getTravelTimeInSecondsToDepot(DistanceType distanceType) {
    return getTravelTimeInSecondsTo(distanceType, vehicle);
  }


  @Override
  public Dto convertToDto() {
    throw new NotImplementedException("ConvertToDTO method is not yet implemented for " + this);
  }

  /* ------------------------------------------ Getters and setters -----------------------*/


  public void setPreviousStandstill(Standstill previousStandstill) {
    super.setPreviousStandstill(previousStandstill);
  }


  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
  }

  public int getDemand() {
    return demand;
  }

  @Override
  public Job safeClone() {
    Job clonedJob = new SintefJob(getId(), getJobType(), getLocation(), getAllowedTimeWindow(), demand, serviceTime, getTimeZoneId());
    return clonedJob;
  }

  @Override
  public String toString() {
    return "SintefJob{" +
        "id=" + getId() +
        ", demand=" + demand +
        ", vehicle=" + vehicle +
        '}';
  }

}
