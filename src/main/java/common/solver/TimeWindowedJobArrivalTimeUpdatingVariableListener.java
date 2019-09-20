package common.solver;

import common.basedomain.DistanceType;
import common.basedomain.Standstill;
import common.basedomain.timewindowed.TimeWindowedJob;
import common.basedomain.timewindowed.TimeWindowedVehicle;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import vrpproblems.sintef.domain.SintefJob;

import java.util.Objects;


public class TimeWindowedJobArrivalTimeUpdatingVariableListener extends BaseVariableListener<TimeWindowedJob> {

  @Override
  public void afterEntityAdded(ScoreDirector scoreDirector, TimeWindowedJob timeWindowedJob) {
    updateArrivalTime(scoreDirector, timeWindowedJob);
  }

  @Override
  public void afterVariableChanged(ScoreDirector scoreDirector, TimeWindowedJob timeWindowedJob) {
    updateArrivalTime(scoreDirector, timeWindowedJob);
  }


  protected void updateArrivalTime(ScoreDirector scoreDirector, TimeWindowedJob sourceCustomer) {
    Standstill previousStandstill = sourceCustomer.getPreviousStandstill();
    Long departureTime = (previousStandstill instanceof TimeWindowedJob)
        ?  ((TimeWindowedJob) previousStandstill).getDepartureTime(): null;

    TimeWindowedJob shadowCustomer = sourceCustomer;
    Long arrivalTime = calculateArrivalTime(shadowCustomer, departureTime);
    while (shadowCustomer != null && !Objects.equals(shadowCustomer.getArrivalTime(), arrivalTime)) {
      scoreDirector.beforeVariableChanged(shadowCustomer, "arrivalTime");
      shadowCustomer.setArrivalTime(arrivalTime);
      scoreDirector.afterVariableChanged(shadowCustomer, "arrivalTime");
      departureTime = shadowCustomer.getDepartureTime();
      shadowCustomer = (TimeWindowedJob) shadowCustomer.getNextJob();
      arrivalTime = calculateArrivalTime(shadowCustomer, departureTime);
    }
  }

  private Long calculateArrivalTime(TimeWindowedJob job, Long previousDepartureTime) {
    if (job == null || job.getPreviousStandstill() == null) {
      return null;
    }
    DistanceType distanceType = job instanceof SintefJob ? DistanceType.STRAIGHT_LINE_DISTANCE : DistanceType.ROAD_DISTANCE;

    if (previousDepartureTime == null) {
      // previous standstill is the vehicle, so we leave from Depot
      long readyTime = job.getPreviousStandstill() instanceof TimeWindowedVehicle
          ? ((TimeWindowedVehicle) job.getPreviousStandstill()).getOperationalTimeWindow().getStart().getEpochSecond() : 0;

      return Math.max(job.getAllowedTimeWindow().getStart().getEpochSecond(),
          readyTime + job.getTravelTimeInSecondsFromPreviousStandstill(distanceType));
    }

    return Math.max(job.getAllowedTimeWindow().getStart().getEpochSecond(),
        previousDepartureTime + job.getTravelTimeInSecondsFromPreviousStandstill(distanceType));
  }
}
