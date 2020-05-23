package vrpproblems.sintef.domain;

import common.optaplanner.basedomain.*;
import org.apache.commons.lang3.NotImplementedException;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;

import java.io.File;
import java.util.List;

@PlanningSolution(solutionCloner = SintefVehicleRoutingSolutionCloner.class)

public class SintefVehicleRoutingSolution implements VehicleRoutingSolution {

  private Shift shift;

  private BendableLongScore score;

  public SintefVehicleRoutingSolution(Shift shift) {
    this.shift = shift;
  }

  public Vehicle getVehicleById(String id) {
    return shift.getVehicleById(id);
  }

  public Job getJobById(String id) {
    return shift.getJobById(id);
  }

  @PlanningScore(bendableHardLevelsSize = 2, bendableSoftLevelsSize = 2)
  public BendableLongScore getScore() {
    return score;
  }

  public void setScore(BendableLongScore score) {
    this.score = score;
  }

  @PlanningEntityCollectionProperty
  @ValueRangeProvider(id = "vehicleRange")
  public List<Vehicle> getVehicles() {
    return shift.getVehicles();
  }

  @PlanningEntityCollectionProperty
  @ValueRangeProvider(id = "jobsRange")
  public List<Job> getJobs() {
    return shift.getJobs();
  }

  @ProblemFactCollectionProperty
  @Override
  public List<Location> getLocations() {
    return shift.getLocations();
  }

  @Override
  public String toString() {
    return "SintefVehicleRoutingSolution{" +
        "shift=" + shift +
        ", score=" + score +
        '}';
  }

  @Override
  public SintefVehicleRoutingSolution safeClone() {
    SintefVehicleRoutingSolution clone = new SintefVehicleRoutingSolution(shift.safeClone());
    clone.setScore(this.getScore());
    return clone;
  }

  @Override
  public void export(File file) {
    throw new NotImplementedException("Export for SintefSolution is not yet implemented");
  }

  @Override
  public String getRoutePlanId() {
    return shift.getId();
  }
}
