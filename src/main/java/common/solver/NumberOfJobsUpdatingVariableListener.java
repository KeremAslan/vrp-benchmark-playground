package common.solver;

import common.basedomain.Job;
import common.basedomain.Standstill;
import common.basedomain.Vehicle;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class NumberOfJobsUpdatingVariableListener extends BaseVariableListener<Standstill> {

  @Override
  public void beforeVariableChanged(ScoreDirector scoreDirector, Standstill standstill) {
    updateNumberOfJobsInRoute(scoreDirector, standstill);
  }

  @Override
  public void afterVariableChanged(ScoreDirector scoreDirector, Standstill standstill) {
    updateNumberOfJobsInRoute(scoreDirector, standstill);
  }

  private void updateNumberOfJobsInRoute(ScoreDirector scoreDirector, Standstill standstill) {
    Vehicle vehicle = standstill.getVehicle();
    if (vehicle != null) {
      int numberOfJobs = 0;
      Job job = vehicle.getNextJob();
      while (job != null) {
        numberOfJobs++;
        job = job.getNextJob();
      }
      scoreDirector.beforeVariableChanged(vehicle, Vehicle.NUMBER_OF_JOBS_VARIABLE_NAME);
      vehicle.setNumberOfJobs(numberOfJobs);
      scoreDirector.afterVariableChanged(vehicle, Vehicle.NUMBER_OF_JOBS_VARIABLE_NAME);
    }
  }
}
