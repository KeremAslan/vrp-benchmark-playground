package common.optaplanner.basedomain.timewindowed;

import common.optaplanner.basedomain.Job;
import common.optaplanner.basedomain.JobType;
import common.optaplanner.basedomain.Location;
import common.optaplanner.solver.TimeWindowedJobArrivalTimeUpdatingVariableListener;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;
import org.threeten.extra.Interval;


import java.time.Instant;
import java.time.ZoneId;

@PlanningEntity
public abstract class TimeWindowedJob extends Job {

  private Interval allowedTimeWindow;
  /** Service time in seconds */
  protected Long serviceTime;

  /** The arrival time at this job. Expressed as long */
  @CustomShadowVariable(variableListenerClass = TimeWindowedJobArrivalTimeUpdatingVariableListener.class,
      // Arguable, to adhere to API specs (although this works), nextCustomer should also be a source,
      // because this shadow must be triggered after nextCustomer (but there is no need to be triggered by nextCustomer)
      sources = {@PlanningVariableReference(variableName = "previousStandstill")})
  private Long arrivalTime;

  private ZoneId timeZoneId;

  public TimeWindowedJob(String id, JobType jobType, Location location, Interval allowedTimeWindow, Long serviceTime, ZoneId timeZoneId) {
    super(id, jobType, location);
    this.allowedTimeWindow = allowedTimeWindow;
    this.serviceTime = serviceTime;
    this.timeZoneId = timeZoneId;
  }

  public Long getDepartureTime() {
    if (arrivalTime == null) {
      return null;
    }
    return Math.max(arrivalTime, getAllowedTimeWindow().getStart().getEpochSecond()) + serviceTime;
  }

  public Long getServiceTime() {
    return serviceTime;
  }

  public boolean isArrivalOnTime() {

    if (arrivalTime != null) {
      // temporarily commented out this logic as ortools treats arrival time valid as long as
      // interval.start <= arrival time <= interval end
      // the method Interval.contains checks only for interval.start <= arrival time < interval end
      // which return false violations
      // -- comment out the code below to use that logic instead.
      //      Instant instantOfArrivalTime = Instant.ofEpochSecond(arrivalTime);
      //      return  getAllowedTimeWindow().contains(instantOfArrivalTime);
      return arrivalTime >= getAllowedTimeWindow().getStart().getEpochSecond() &&
              arrivalTime <= getAllowedTimeWindow().getEnd().getEpochSecond();
    }
    return false;
  }

  public Interval getAllowedTimeWindow() {
    return allowedTimeWindow;
  }


  public Long getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(Long arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public ZoneId getTimeZoneId() {
    return timeZoneId;
  }
}
