package common.optaplanner.basedomain;



import org.optaplanner.core.api.score.Score;

import java.io.File;
import java.util.List;
import java.util.Map;


public interface VehicleRoutingSolution {

  VehicleRoutingSolution safeClone();

  List<Vehicle> getVehicles();

  List<Job> getJobs();

  List<Location> getLocations();

  Vehicle getVehicleById(String id);

  Job getJobById(String id);

  Score getScore();

  static void setPreviousStandstill(Job job, Job clonedJob, Map<String, Vehicle> clonedVehicles, Map<String, Job> clonedJobs) {
    if (job.getPreviousStandstill() != null) {
      Standstill previousStandstill;
      if (job.getPreviousStandstill() instanceof Vehicle) {
        previousStandstill =  clonedVehicles.get(job.getPreviousStandstill().getId());
      } else {
        previousStandstill = clonedJobs.get(job.getPreviousStandstill().getId());
      }
      clonedJob.setPreviousStandstill(previousStandstill);
    }
  }


  static void setNextJob(Job job, Job clonedJob, Map<String, Job> clonedJobs) {
    if (job.getNextJob() != null) {
      Job clonedNextJob = clonedJobs.get(job.getNextJob().getId());
      clonedJob.setNextJob(clonedNextJob);
    }
  }

  void export(File file);

  String getRoutePlanId();
}
