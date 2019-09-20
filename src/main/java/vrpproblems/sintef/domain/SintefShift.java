package vrpproblems.sintef.domain;

import common.basedomain.Job;
import common.basedomain.Shift;
import common.basedomain.Vehicle;
import common.basedomain.VehicleRoutingSolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SintefShift extends Shift {

  public SintefShift(String id, List<Vehicle> vehicles, List<Job> jobs) {
    super(id, vehicles, jobs);
  }


  @Override
  //TechDebt: Time complexity of this method is O(2(m+k)) where m is number of vehicles
  // and k is number of jobs. A different implementation can yield O(m+k) at the expense
  // of readability. Try to implement that.
  public Shift safeClone() {

    Map<String, Vehicle> clonedVehicles = new HashMap<>();
    for (Vehicle vehicle: getVehicles()) {
      clonedVehicles.put(vehicle.getId(), vehicle.safeClone());
    }

    Map<String, Job> clonedJobs = new HashMap<>();
    for (Job job: getJobs()) {
      clonedJobs.put(job.getId(), job.safeClone());
    }

    for (Vehicle vehicle: getVehicles()) {
      Vehicle clonedVehicle = clonedVehicles.get(vehicle.getId());
      clonedVehicle.setNextJob(vehicle.getNextJob());
      clonedVehicle.setNumberOfJobs(vehicle.getNumberOfJobs());
    }

    for (Job job: getJobs()) {
      SintefJob sintefJob = (SintefJob) job;
      SintefJob clonedJob = (SintefJob) clonedJobs.get(job.getId());

      VehicleRoutingSolution.setPreviousStandstill(job, clonedJob, clonedVehicles, clonedJobs);

      VehicleRoutingSolution.setNextJob(job, clonedJob, clonedJobs);



      if (job.getVehicle() != null) {
        Vehicle clonedVehicle = clonedVehicles.get(job.getVehicle().getId());
        clonedJob.setVehicle(clonedVehicle);
      }
      clonedJob.setArrivalTime(sintefJob.getArrivalTime());
    }

    for (Vehicle vehicle: getVehicles()) {
      if (vehicle.getNextJob() != null) {
        Vehicle clonedVehicle = clonedVehicles.get(vehicle.getId());
        Job jobToSet = clonedJobs.get(vehicle.getNextJob().getId());
        clonedVehicle.setNextJob(jobToSet);
      }
    }
    return new SintefShift(getId(), new ArrayList<>(clonedVehicles.values()), new ArrayList<>(clonedJobs.values()));
  }
}
