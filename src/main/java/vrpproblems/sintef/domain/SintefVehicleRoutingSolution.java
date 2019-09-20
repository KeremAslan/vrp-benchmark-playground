package vrpproblems.sintef.domain;

import common.basedomain.Job;
import common.basedomain.Shift;
import common.basedomain.Vehicle;
import common.basedomain.VehicleRoutingSolution;
import org.apache.commons.lang3.NotImplementedException;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;

import java.io.File;
import java.util.List;

@PlanningSolution(solutionCloner = SintefVehicleRoutingSolutionCloner.class)

public class SintefVehicleRoutingSolution implements VehicleRoutingSolution {

  private Shift shift;

  private HardMediumSoftLongScore score;

  public SintefVehicleRoutingSolution(Shift shift) {
    this.shift = shift;
  }


  @PlanningScore
  public HardMediumSoftLongScore getScore() {
    return score;
  }

  public void setScore(HardMediumSoftLongScore score) {
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
