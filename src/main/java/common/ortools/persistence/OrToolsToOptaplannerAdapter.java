package common.ortools.persistence;

import com.google.ortools.constraintsolver.*;
import common.optaplanner.basedomain.*;
import common.optaplanner.basedomain.timewindowed.TimeWindowedJob;
import vrpproblems.sintef.domain.SintefShift;
import vrpproblems.sintef.domain.SintefVehicleRoutingSolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrToolsToOptaplannerAdapter {

    private Assignment assignment;
    private VehicleRoutingSolution optaplannerModel;
    private RoutingIndexManager routingIndexManager;
    private RoutingModel routingModel;

    public OrToolsToOptaplannerAdapter(Assignment assignment,
                                       RoutingIndexManager routingIndexManager,
                                       RoutingModel routingModel,
                                       VehicleRoutingSolution optaplannerModel) {
        this.assignment = assignment;
        this.routingIndexManager = routingIndexManager;
        this.routingModel = routingModel;
        this.optaplannerModel = optaplannerModel;
    }

    public VehicleRoutingSolution convert() {
//        this.assignment.
        List<Vehicle> vehicles = new ArrayList<>();
        List<Job> jobs = new ArrayList<>();
        List<Location> locations = new ArrayList<>();

        // extract routes from or-tools
        Map<Long, List<Long>> routesMap = new HashMap<>();
        Map<Long, Long> arrivalTimeMap = new HashMap<>();

        RoutingDimension timeDimension = routingModel.getMutableDimension("TravelTime");
        for (int vehicleNo = 0; vehicleNo < 1000; vehicleNo++) {
            long index = routingModel.start(vehicleNo);
            long vehicleIndex = index;
            List<Long> route = new ArrayList<>();
            while (!routingModel.isEnd(index)) {
                if (!routingModel.isStart(index)) {
                    route.add(index);
                }
                IntVar timeVar = timeDimension.cumulVar(index);
                index = assignment.value(routingModel.nextVar(index));
                long arrivalTime = assignment.value(timeVar);

                arrivalTimeMap.put(index, arrivalTime);
            }
            routesMap.put(Long.valueOf(vehicleNo), route);
        }

        // populate optaplanner domain models
        Map<Vehicle, List<Job>> routePlan = new HashMap<>();
        Map<Job, Long> jobToOrToolsIndexMap = new HashMap<>();
        for (Map.Entry<Long, List<Long>> routes : routesMap.entrySet()) {
            Long vehicleId = routingIndexManager.nodeToIndex(routes.getKey().intValue());
            List<Long> route = routes.getValue();
            Vehicle vehicle = optaplannerModel.getVehicleById(String.valueOf(vehicleId));

            if (vehicle != null) {
                vehicles.add(vehicle);
                List<Job> optaplannerSolutionRoute = new ArrayList<>();
                for (int i = 0; i < route.size(); i++) {
                    Long jobId = route.get(i);
                    Job job = optaplannerModel.getJobById(String.valueOf(jobId));
                    jobToOrToolsIndexMap.put( job, route.get(i));
                    optaplannerSolutionRoute.add(job);
                }
                routePlan.put(vehicle, optaplannerSolutionRoute);
            }

        }

        // fix planning and shadow variables
        for (Map.Entry<Vehicle, List<Job>> entry : routePlan.entrySet()) {
            Vehicle vehicle = entry.getKey();
            List<Job> route = entry.getValue();
            Standstill previousStandstill = vehicle;
            for (int jobIndex=0; jobIndex< route.size()-1; jobIndex++) {
                TimeWindowedJob job = (TimeWindowedJob) route.get(jobIndex);
                job.setPreviousStandstill(previousStandstill);
                Job nextJob = route.get(jobIndex+1);
                job.setNextJob(nextJob);

                Long ortoolsIndex = jobToOrToolsIndexMap.get(job);
                job.setArrivalTime(arrivalTimeMap.get(ortoolsIndex));
                previousStandstill = job;
            }
        }


        Shift shift = new SintefShift(
                "Or-Tools Solution",
                vehicles,
                jobs,
                locations
                );

        SintefVehicleRoutingSolution solution = new SintefVehicleRoutingSolution(shift);
        this.optaplannerModel = solution;
        return solution;
    }

}
