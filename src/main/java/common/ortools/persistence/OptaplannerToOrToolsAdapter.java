package common.ortools.persistence;

import common.optaplanner.basedomain.DistanceType;
import common.optaplanner.basedomain.Job;
import common.optaplanner.basedomain.Location;
import common.optaplanner.basedomain.VehicleRoutingSolution;
import common.optaplanner.basedomain.timewindowed.TimeWindowedJob;
import org.threeten.extra.Interval;

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
        timeWindows = new long[optaPlannnerModel.getJobs().size()+1][2];
        for (Job job1 : optaPlannnerModel.getJobs()) {
            TimeWindowedJob timeWindowedJob = (TimeWindowedJob) job1;
            int indexOfJob1 = optaPlannnerModel.getJobs().indexOf(job1) + 1;
            Interval interval = timeWindowedJob.getAllowedTimeWindow();
            timeWindows[indexOfJob1] = new long[]{interval.getStart().getEpochSecond(), interval.getEnd().getEpochSecond()};

            long[] travelTimesForJob = new long[optaPlannnerModel.getJobs().size() + 1];
            for (Job job2 : optaPlannnerModel.getJobs()) {
                int indexOfJob2 = optaPlannnerModel.getJobs().indexOf(job2) + 1;
                Long travelTimeBetweenJob1AndJob2 = job1.getTravelTimeInSecondsTo(DistanceType.STRAIGHT_LINE_DISTANCE, job2);
                travelTimesForJob[indexOfJob2] = travelTimeBetweenJob1AndJob2;
                timeMatrix[indexOfJob1] = travelTimesForJob;
            }
        }
    }
}
