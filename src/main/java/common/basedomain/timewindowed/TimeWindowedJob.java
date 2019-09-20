package common.basedomain.timewindowed;

import common.basedomain.Job;
import common.basedomain.JobType;
import common.basedomain.Location;
import common.solver.TimeWindowedJobArrivalTimeUpdatingVariableListener;
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
      Instant instantOfArrivalTime = Instant.ofEpochSecond(arrivalTime);

      return  getAllowedTimeWindow().contains(instantOfArrivalTime);

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
