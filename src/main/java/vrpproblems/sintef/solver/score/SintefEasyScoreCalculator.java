package vrpproblems.sintef.solver.score;

import common.optaplanner.basedomain.DistanceType;
import common.optaplanner.basedomain.Job;
import common.optaplanner.basedomain.Standstill;
import common.optaplanner.basedomain.Vehicle;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import vrpproblems.sintef.domain.SintefJob;
import vrpproblems.sintef.domain.SintefVehicle;
import vrpproblems.sintef.domain.SintefVehicleRoutingSolution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SintefEasyScoreCalculator implements EasyScoreCalculator<SintefVehicleRoutingSolution> {

  @Override
  public Score calculateScore(SintefVehicleRoutingSolution sintefSolution) {

    int timeWindowViolations = 0;
    int capacityViolations = 0;
    int hardScore = 0;
    int numberOfVehicles = 0;
    long totalDistance = 0L;

    Map<Vehicle, Integer> vehicleDemandMap = new HashMap<>();
    List<Vehicle> vehicles = sintefSolution.getVehicles();

    for(Vehicle vehicle: vehicles) {
      vehicleDemandMap.put(vehicle, 0);
    }

    for (Job job: sintefSolution.getJobs()) {
      SintefJob sintefJob = (SintefJob) job;

      Standstill previousStandstill = sintefJob.getPreviousStandstill();

      Vehicle vehicle = sintefJob.getVehicle();

      if (previousStandstill != null) {
        totalDistance -= Math.round(sintefJob.getTravelTimeInSecondsFromPreviousStandstill(DistanceType.STRAIGHT_LINE_DISTANCE));

        vehicleDemandMap.put(vehicle, vehicleDemandMap.get(vehicle) + sintefJob.getDemand());

        if (sintefJob.getNextJob() == null) {
          totalDistance -= sintefJob.getTravelTimeInSecondsToDepot(DistanceType.STRAIGHT_LINE_DISTANCE);
        }

        if(!sintefJob.isArrivalOnTime()) {
          timeWindowViolations++;
          hardScore--;
        }
      }

    }

    for (Map.Entry<Vehicle, Integer> entry: vehicleDemandMap.entrySet()) {
      SintefVehicle sintefVehicle = (SintefVehicle) entry.getKey();
      int capacity = sintefVehicle.getCapacity();
      int utilisedCapacity = entry.getValue();

      if (utilisedCapacity > capacity) {
        capacityViolations++;
        hardScore -= (utilisedCapacity - capacity);
      }

      if (utilisedCapacity > 0) {
        numberOfVehicles--;
      }
    }
    System.out.println("Total time window violations " + timeWindowViolations + " total capacity violations " + capacityViolations);
    return HardMediumSoftLongScore.valueOf(hardScore, numberOfVehicles, totalDistance);
  }
}
