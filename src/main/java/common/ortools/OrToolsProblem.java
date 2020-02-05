package common.ortools;

import com.google.ortools.constraintsolver.RoutingDimension;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import common.optaplanner.basedomain.Vehicle;
import vrpproblems.sintef.domain.SintefVehicle;

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

  public OrToolsProblem(long[][] timeWindows, long[][] timeMatrix, int vehicleNumber, int depotIndex, SintefVehicle vehicle) {
    this.timeWindows = timeWindows;
    this.timeMatrix = timeMatrix;
    this.vehicleNumber = vehicleNumber;
    this.depotIndex = depotIndex;
    this.vehicle = vehicle;
  }


  private void buildProblem() {
    RoutingIndexManager routingIndexManager = new RoutingIndexManager(timeMatrix.length, vehicleNumber, depotIndex);
    RoutingModel routingModel = new RoutingModel(routingIndexManager);
    final int transitCallBackIndex = routingModel.registerTransitCallback(
        (long fromIndex, long toIndex) -> {
          // Convert from routing variable Index to user NodeIndex
          int fromNode = routingIndexManager.indexToNode(fromIndex);
          int toNode = routingIndexManager.indexToNode(toIndex);
          return timeMatrix[fromNode][toNode];
        }
    );
    routingModel.setArcCostEvaluatorOfAllVehicles(transitCallBackIndex);
    routingModel.addDimension(transitCallBackIndex, 30, vehicle.getCapacity(), false, "Time");
    RoutingDimension timeDimension = routingModel.getMutableDimension("Time");

    // add time window constraints for each location except depot.
    for (int i=1; i < timeWindows.length; i++) {
      long index = routingIndexManager.nodeToIndex(i);
      timeDimension.cumulVar(index).setRange(timeWindows[i][0], timeWindows[i][1]);
    }
  }

}
