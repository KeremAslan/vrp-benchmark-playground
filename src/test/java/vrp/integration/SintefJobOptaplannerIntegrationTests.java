package vrp.integration;


import common.basedomain.DistanceType;
import common.basedomain.Job;
import common.basedomain.Vehicle;
import common.basedomain.VehicleRoutingSolution;
import common.basedomain.timewindowed.TimeWindowedJob;
import vrp.testhelpers.OptaplannerHelper;
import org.junit.Assert;
import org.junit.Test;
import vrpproblems.ProblemType;

public class SintefJobOptaplannerIntegrationTests {


  @Test
  public void arrivalTimeShouldReturnSameValueAsDepartureTimePlusDistanceFromPreviousStandstillWhenJobHasAPreviousStandstill() {
    VehicleRoutingSolution problem = OptaplannerHelper.buildOptaplannerProblem(ProblemType.SINTEF, 1, 5);
    VehicleRoutingSolution solution = OptaplannerHelper.runOptaplannerWithBasicConfiguration(ProblemType.SINTEF, problem);

    Vehicle vehicle = solution.getVehicles().get(0);

    TimeWindowedJob firstJob = (TimeWindowedJob) vehicle.getNextJob();
    TimeWindowedJob secondJob = (TimeWindowedJob) firstJob.getNextJob();

    Long actual = Math.max(
        firstJob.getDepartureTime() + secondJob.getTravelTimeInSecondsFromPreviousStandstill(DistanceType.STRAIGHT_LINE_DISTANCE),
        secondJob.getAllowedTimeWindow().getStart().getEpochSecond()
    );

    Assert.assertEquals(secondJob.getArrivalTime(), actual);

  }

  @Test
  public void getVehicleShouldReturnTheVehicleThatIsARoutesAnchor() {
    VehicleRoutingSolution problem = OptaplannerHelper.buildOptaplannerProblem(ProblemType.SINTEF, 1, 5);
    VehicleRoutingSolution solution = OptaplannerHelper.runOptaplannerWithBasicConfiguration(ProblemType.SINTEF, problem);

    Vehicle vehicle = solution.getVehicles().get(0);
    Job job = vehicle.getNextJob();

    while (job != null) {
      Assert.assertEquals(job.getVehicle(), vehicle);
      job = job.getNextJob();
    }
  }

  @Test
  public void getNumberOfJobsShouldReturnFiveWhenAVehicleHasFiveJobs() {
    VehicleRoutingSolution problem = OptaplannerHelper.buildOptaplannerProblem(ProblemType.SINTEF, 1, 5);
    VehicleRoutingSolution solution = OptaplannerHelper.runOptaplannerWithBasicConfiguration(ProblemType.SINTEF, problem);

    Vehicle vehicle = solution.getVehicles().get(0);
    Integer numberOfJobs = vehicle.getNumberOfJobs();

    Assert.assertEquals(5, numberOfJobs.intValue());
  }


}
