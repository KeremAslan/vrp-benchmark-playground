package common.optaplanner.basedomain;

import common.optaplanner.persistence.dto.Dto;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

@PlanningEntity
public abstract class Job implements Standstill {

  private String id;
  private JobType jobType;

  private Location location;

  // planning variable
  /** previousStandstill is an Optaplanner planning variable. previousStandstill refers to the previous node in the chain of visits */
  protected Standstill previousStandstill;
  // Shadow Variable
  protected Vehicle vehicle;
  protected Job nextJob;

  public Job(String id, JobType jobType, Location location) {
    this.id = id;
    this.jobType = jobType;
    this.location = location;
  }

  public abstract Long getTravelTimeInSecondsFromPreviousStandstill(DistanceType distanceType);

  public abstract Dto convertToDto();

  @Override
  @AnchorShadowVariable(sourceVariableName = "previousStandstill")
  public Vehicle getVehicle() {
    return vehicle;
  }

  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
  }

  @Override
  @PlanningId
  public String getId() {
    return id;
  }

  public JobType getJobType() {
    return jobType;
  }

  public Location getLocation() {
    return location;
  }

  @PlanningVariable(valueRangeProviderRefs = {"vehicleRange", "jobsRange"}, graphType = PlanningVariableGraphType.CHAINED)
  public Standstill getPreviousStandstill() {
    return previousStandstill;
  }

  public void setPreviousStandstill(Standstill previousStandstill) {
    this.previousStandstill = previousStandstill;
  }

  @Override
  public Job getNextJob() {
    return nextJob;
  }

  @Override
  public void setNextJob(Job nextStandstill) {
    this.nextJob = nextStandstill;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public abstract Job safeClone();




}
