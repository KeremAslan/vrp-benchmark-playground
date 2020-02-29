package common.ortools;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.IntVar;
import com.google.ortools.constraintsolver.RoutingDimension;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import common.optaplanner.basedomain.Vehicle;
import vrpproblems.sintef.domain.SintefVehicle;
import com.google.ortools.constraintsolver.main;

public class OrToolsProblem {

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

  private long[] vehicleCapacities;
  private long[] demands;

  private RoutingModel routingModel;
  private Assignment solution;

  public OrToolsProblem(long[][] timeWindows, long[][] timeMatrix, int vehicleNumber, int depotIndex, SintefVehicle vehicle,
                        long[] vehicleCapacities, long[] demands) {
    this.timeWindows = timeWindows;
    this.timeMatrix = timeMatrix;
    this.vehicleNumber = vehicleNumber;
    this.depotIndex = depotIndex;
    this.vehicle = vehicle;
    this.vehicleCapacities = vehicleCapacities;
    this.demands = demands;
  }


  public void buildProblem() {
    RoutingIndexManager routingIndexManager = new RoutingIndexManager(timeMatrix.length, vehicleNumber, depotIndex);
    routingModel = new RoutingModel(routingIndexManager);

    // minimise total travel time
    final int transitCallBackIndex = routingModel.registerTransitCallback(
        (long fromIndex, long toIndex) -> {
          // Convert from routing variable Index to user NodeIndex
          int fromNode = routingIndexManager.indexToNode(fromIndex);
          int toNode = routingIndexManager.indexToNode(toIndex);
          return timeMatrix[fromNode][toNode];
        }
    );
    routingModel.setArcCostEvaluatorOfAllVehicles(transitCallBackIndex);
    routingModel.addDimension(transitCallBackIndex, Integer.MAX_VALUE, Integer.MAX_VALUE, false, "TravelTime");
    RoutingDimension timeDimension = routingModel.getMutableDimension("TravelTime");

    // add time window constraints for each location except depot.
    for (int i=1; i < timeWindows.length; i++) {
      long index = routingIndexManager.nodeToIndex(i);
      timeDimension.cumulVar(index).setRange(timeWindows[i][0], timeWindows[i][1]);
    }

    // add time window constraints for each vehicle's start node
    for (int i=0 ; i < vehicleNumber; i++) {
      long index = routingModel.start(i);
      long[] timeWindowsForDepot = timeWindows[0];
      timeDimension.cumulVar(index).setRange(timeWindowsForDepot[0], timeWindowsForDepot[1]);
    }

    // add vehicle capacity constraints
    int demandCallBackIndex = routingModel.registerUnaryTransitCallback((long fromIndex) -> {
      int fromNode = routingIndexManager.indexToNode(fromIndex);
      return demands[fromNode];
    });

    routingModel.addDimensionWithVehicleCapacity(
            demandCallBackIndex,
            0,
            vehicleCapacities,
            true,
            "Capacity"
    );


    // set service times
    // Or tools does not support seperate service times, hence this is added to the travel time matrix.

    // minimise number of used vehicle
    routingModel.setFixedCostOfAllVehicles(1);


  }

  public void solve(RoutingSearchParameters searchParameters) {
    solution = routingModel.solveWithParameters(searchParameters);
  }

  public Assignment getSolution() {
    return solution;
  }
}
