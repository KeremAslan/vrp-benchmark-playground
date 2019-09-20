package vrpproblems.sintef.domain;


import org.optaplanner.core.api.domain.solution.cloner.SolutionCloner;

public class SintefVehicleRoutingSolutionCloner implements SolutionCloner<SintefVehicleRoutingSolution> {

  @Override
  public SintefVehicleRoutingSolution cloneSolution(SintefVehicleRoutingSolution original) {
    return original.safeClone();
  }
}
