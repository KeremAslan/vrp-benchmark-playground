package vrpproblems.sintef.solver.score;

import common.optaplanner.basedomain.DistanceType;
import common.optaplanner.basedomain.Job;
import common.optaplanner.basedomain.Standstill;
import common.optaplanner.basedomain.Vehicle;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator;
import vrpproblems.sintef.domain.SintefJob;
import vrpproblems.sintef.domain.SintefVehicle;
import vrpproblems.sintef.domain.SintefVehicleRoutingSolution;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SintefIncrementalScoreCalculator implements IncrementalScoreCalculator<SintefVehicleRoutingSolution> {


    private long numberOfTimeWindowViolations = 0;
    private long numberOfCapacityViolations = 0;
    private long numberOfUsedVehicles = 0;
    private Long totalDriveTimeInSeconds = 0L;
    Map<Vehicle, Long> totalConsumedDemand;

    @Override
    public void resetWorkingSolution(SintefVehicleRoutingSolution workingSolution) {
        numberOfUsedVehicles = 0;
        totalDriveTimeInSeconds = 0L;
        totalConsumedDemand = new HashMap<>();

        for (Vehicle vehicle: workingSolution.getVehicles()) {
            totalConsumedDemand.put(vehicle, 0L);
        }

        for (Job job: workingSolution.getJobs()) {
            SintefJob sintefJob = (SintefJob) job;
            insertPreviousStandstill(sintefJob);
            insertArrivalTime(sintefJob);
            insertVehicle(sintefJob);
        }
    }

    @Override
    public void beforeEntityAdded(Object o) {
        // do nothing
    }

    @Override
    public void afterEntityAdded(Object o) {
        if (o instanceof Vehicle) {
            return;
        }

        SintefJob sintefJob = (SintefJob) o;
        insertPreviousStandstill(sintefJob);
        insertVehicle(sintefJob);
        insertArrivalTime(sintefJob);
    }

    @Override
    public void beforeVariableChanged(Object o, String s) {
        if (o instanceof  Vehicle) {
            return;
        }

        SintefJob sintefJob = (SintefJob) o;

        switch (s) {
            case "previousStandstill":
                retractPreviousStandstill(sintefJob);
                break;
            case "vehicle":
                retractVehicle(sintefJob);
                break;
            case "nextJob":
                retractNextJob(sintefJob);
                break;
            case "arrivalTime":
                retractArrivalTime(sintefJob);
                break;
                default:
            throw new IllegalArgumentException("Unsupported variable name for " + s);
        }
    }

    @Override
    public void afterVariableChanged(Object o, String s) {
        if (o instanceof  Vehicle) {
            return;
        }

        SintefJob sintefJob = (SintefJob) o;

        switch (s) {
            case "previousStandstill":
                insertPreviousStandstill(sintefJob);
                break;
            case "vehicle":
                insertVehicle(sintefJob);
                break;
            case "nextJob":
                insertNextJob(sintefJob);
                break;
            case "arrivalTime":
                insertArrivalTime(sintefJob);
                break;
            default:
                throw new IllegalArgumentException("Unsupported variable name for " + s);
        }
    }

    @Override
    public void beforeEntityRemoved(Object o) {

    }

    @Override
    public void afterEntityRemoved(Object o) {

    }

    private void insertVehicle(SintefJob job) {

        SintefVehicle vehicle =  (SintefVehicle) job.getVehicle();
        if (vehicle != null) {
            Long oldDemand = totalConsumedDemand.get(vehicle);
            Long newDemand = oldDemand + job.getDemand();

            if (newDemand > vehicle.getCapacity()) {
                numberOfCapacityViolations -= (newDemand - vehicle.getCapacity());
            }
            totalConsumedDemand.put(vehicle, newDemand);
        }
    }

    private void retractVehicle(SintefJob job) {
        SintefVehicle vehicle =  (SintefVehicle) job.getVehicle();
        if (vehicle != null) {
            Long oldDemand = totalConsumedDemand.get(vehicle);
            Long newDemand = oldDemand + job.getDemand();

            if (newDemand > vehicle.getCapacity()) {
                numberOfCapacityViolations += (newDemand - vehicle.getCapacity());
            }
            totalConsumedDemand.put(vehicle, newDemand);
        }
    }

    private void insertPreviousStandstill(SintefJob job) {
        Standstill standstill = job.getPreviousStandstill();
        if (standstill != null) {
            totalDriveTimeInSeconds -= job.getTravelTimeInSecondsFromPreviousStandstill(DistanceType.STRAIGHT_LINE_DISTANCE);
        }
    }

    private void retractPreviousStandstill(SintefJob job) {
        Standstill standstill = job.getPreviousStandstill();
        if (standstill != null) {
            totalDriveTimeInSeconds += job.getTravelTimeInSecondsFromPreviousStandstill(DistanceType.STRAIGHT_LINE_DISTANCE);
        }
    }

    private void insertArrivalTime(SintefJob job) {
        Long arrivalTime = job.getArrivalTime();
        if (arrivalTime != null) {
            if (Instant.ofEpochSecond(arrivalTime).isAfter(job.getAllowedTimeWindow().getEnd())) {
                numberOfTimeWindowViolations--;
            }

            if (Instant.ofEpochSecond(arrivalTime).isBefore(job.getAllowedTimeWindow().getStart())) {
                numberOfTimeWindowViolations--;
            }
        }
    }

    private void retractArrivalTime(SintefJob job) {
        Long arrivalTime = job.getArrivalTime();
        if (arrivalTime != null) {
            if (Instant.ofEpochSecond(arrivalTime).isAfter(job.getAllowedTimeWindow().getEnd())) {
                numberOfTimeWindowViolations++;
            }

            if (Instant.ofEpochSecond(arrivalTime).isBefore(job.getAllowedTimeWindow().getStart())) {
                numberOfTimeWindowViolations++;
            }
        }
    }

    private void insertNextJob(SintefJob job) {
        Vehicle vehicle = job.getVehicle();

        if (job.getNextJob() == null) {

        }
    }


    private void retractNextJob(SintefJob job) {
        Vehicle vehicle = job.getVehicle();

        if (job.getNextJob() == null) {

        }
    }



    @Override
    public Score calculateScore() {
        return HardMediumSoftLongScore.of(
                -(numberOfCapacityViolations + numberOfTimeWindowViolations),
                -numberOfUsedVehicles,
                -totalDriveTimeInSeconds);
    }
}
