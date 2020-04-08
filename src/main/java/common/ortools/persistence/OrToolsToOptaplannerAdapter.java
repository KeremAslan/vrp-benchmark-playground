package common.ortools.persistence;

import com.google.ortools.constraintsolver.*;
import common.optaplanner.basedomain.*;
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
            routesMap.put(vehicleIndex, route);
        }

        for (Map.Entry<Long, List<Long>> routes : routesMap.entrySet()) {
            Long vehicleId = routes.getKey();
            List<Long> route = routes.getValue();
            Vehicle vehicle = optaplannerModel.getVehicleById(String.valueOf(vehicleId));
            System.out.println("Vehicle id " + vehicleId);
            if (route.size() > 0) {
                System.out.println("piet");
            }
            List<Job> optaroute = new ArrayList<>();
            for(int i=0; i < route.size(); i++) {
                Long jobId = route.get(i);
                Job job = optaplannerModel.getJobById(String.valueOf(jobId));
                if (i == 0) {
                    job.setPreviousStandstill(vehicle);
                    vehicle.setNextJob(job);
                }
                optaroute.add(job);
                if (i > 0) {
                    Job previousJob = optaplannerModel.getJobById(String.valueOf(route.get(i-1)));
                    previousJob.setNextJob(job);
                    job.setPreviousStandstill(previousJob);
                }
            }
            System.out.println("finished a route");

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
