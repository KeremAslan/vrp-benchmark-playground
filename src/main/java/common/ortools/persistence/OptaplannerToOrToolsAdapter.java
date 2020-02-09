package common.ortools.persistence;

import common.optaplanner.basedomain.Depot;
import common.optaplanner.basedomain.DistanceType;
import common.optaplanner.basedomain.Job;
import common.optaplanner.basedomain.Location;
import common.optaplanner.basedomain.Vehicle;
import common.optaplanner.basedomain.VehicleRoutingSolution;
import common.optaplanner.basedomain.timewindowed.TimeWindowedJob;
import common.ortools.OrToolsProblem;
import org.threeten.extra.Interval;
import vrpproblems.sintef.domain.SintefDepot;
import vrpproblems.sintef.domain.SintefVehicle;

public class OptaplannerToOrToolsAdapter {

    private VehicleRoutingSolution optaPlannnerModel;

    // or-tools models
    /** Time windows is  for the locations. Index 0 is that time windows of the depot*/
    private long[][] timeWindows;
    /** Array of travel times between locations. Index 0 is the travel distances from depot.*/
    private long[][] timeMatrix;
    /** Number of vehicles*/
    private int vehicleNumber;
    /** Index of depot.*/
    private int depotIndex;
    /** A vehicle model used in the routing problem*/
    private SintefVehicle vehicle;


    public OptaplannerToOrToolsAdapter(VehicleRoutingSolution optaPlannnerModel) {
        this.optaPlannnerModel = optaPlannnerModel;
        init();
    }

    private void init() {
        setTimeWindowsAndTimeMatrix(optaPlannnerModel);
        vehicleNumber = optaPlannnerModel.getVehicles().size();
        depotIndex = 0;
    }


    private void setTimeWindowsAndTimeMatrix(VehicleRoutingSolution optaPlannnerModel) {
        int numberOfJobs = optaPlannnerModel.getJobs().size();
        int totalNumberOfNodes = numberOfJobs + 1;

        timeMatrix = new long[totalNumberOfNodes][];
        // set for depot
        // although the modelling for optaplanner is done such it allows for start and end depots, the sintef problem has only
        // a single depot
        vehicle = (SintefVehicle) optaPlannnerModel.getVehicles().get(0);
        Depot startDepot = vehicle.getStartDepot();
        long[] travelTimesForDepot = new long[totalNumberOfNodes];
        travelTimesForDepot[0] = 0L;
        for (Job job : optaPlannnerModel.getJobs()) {
            Long travelTime = startDepot.getDistanceTo(DistanceType.STRAIGHT_LINE_DISTANCE, job.getLocation());
            int indexofJob = optaPlannnerModel.getJobs().indexOf(job) + 1;
            travelTimesForDepot[indexofJob] = travelTime;
        }
        timeMatrix[depotIndex] = travelTimesForDepot;

        timeWindows = new long[totalNumberOfNodes][2];

        SintefDepot sintefDepot = (SintefDepot) startDepot;
        timeWindows[depotIndex] = new long[]{
            sintefDepot.getOperationalTimeWindow().getStart().getEpochSecond(), sintefDepot.getOperationalTimeWindow().getEnd().getEpochSecond()};
        for (Job job1 : optaPlannnerModel.getJobs()) {
            TimeWindowedJob timeWindowedJob = (TimeWindowedJob) job1;
            int indexOfJob1 = optaPlannnerModel.getJobs().indexOf(job1) + 1;
            Interval interval = timeWindowedJob.getAllowedTimeWindow();
            timeWindows[indexOfJob1] = new long[]{interval.getStart().getEpochSecond(), interval.getEnd().getEpochSecond()};

            long[] travelTimesForJob = new long[totalNumberOfNodes];
            travelTimesForJob[0] = job1.getTravelTimeInSecondsTo(DistanceType.STRAIGHT_LINE_DISTANCE, vehicle);
            for (Job job2 : optaPlannnerModel.getJobs()) {
                int indexOfJob2 = optaPlannnerModel.getJobs().indexOf(job2) + 1;
                Long travelTimeBetweenJobs = job1.getTravelTimeInSecondsTo(DistanceType.STRAIGHT_LINE_DISTANCE, job2);
                travelTimesForJob[indexOfJob2] = travelTimeBetweenJobs;
                timeMatrix[indexOfJob1] = travelTimesForJob;
            }
        }


    }


   public OrToolsProblem getOrToolsProblem() {
        return new OrToolsProblem(
            timeWindows,
            timeMatrix,
            vehicleNumber,
            depotIndex,
            vehicle
        );
   }
}
