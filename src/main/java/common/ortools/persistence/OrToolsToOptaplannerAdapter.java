package common.ortools.persistence;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import common.optaplanner.basedomain.*;
import common.ortools.OrToolsProblem;
import vrpproblems.sintef.domain.SintefShift;
import vrpproblems.sintef.domain.SintefVehicleRoutingSolution;

import java.util.ArrayList;
import java.util.List;

public class OrToolsToOptaplannerAdapter {

    private Assignment assignment;
    private VehicleRoutingSolution solution;
    private RoutingIndexManager routingIndexManager;
    private RoutingModel routingModel;

    public OrToolsToOptaplannerAdapter(Assignment assignment,
                                       RoutingIndexManager routingIndexManager,
                                       RoutingModel routingModel) {
        this.assignment = assignment;
        this.routingIndexManager = routingIndexManager;
        this.routingModel = routingModel;
    }

    public VehicleRoutingSolution convert() {
        List<Vehicle> vehicles = new ArrayList<>();
        List<Job> jobs = new ArrayList<>();
        List<Location> locations = new ArrayList<>();

        for (int vehicleNo = 0; vehicleNo < 1000; vehicleNo++) {
            long index = routingModel.start(vehicleNo);

            while (!routingModel.isEnd(index)) {

            }
        }

        Shift shift = new SintefShift(
                "Or-Tools Solution",
                vehicles,
                jobs,
                locations
                );

        SintefVehicleRoutingSolution solution = new SintefVehicleRoutingSolution(shift);
        this.solution = solution;
        return solution;
    }

}
