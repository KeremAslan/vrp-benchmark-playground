package common.ortools;

import com.google.ortools.constraintsolver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vrpproblems.sintef.domain.SintefVehicle;

import java.util.HashSet;
import java.util.Set;

public class OrToolsProblem {


  private static final Logger LOG = LoggerFactory.getLogger(OrToolsProblem.class);

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
  private RoutingIndexManager routingIndexManager;

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

  private void setCapacityConstrainsts() {
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
  }

  private void setTimeWindowConstraints() {
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

    for(int i=0; i < vehicleNumber; i++) {
      routingModel.addVariableMinimizedByFinalizer(timeDimension.cumulVar(routingModel.start(i)));
      routingModel.addVariableMinimizedByFinalizer(timeDimension.cumulVar(routingModel.end(i)));
    }


  }

  private void setTravelTimeMatrix() {
    // minimise total travel time
    final int transitCallBackIndex = routingModel.registerTransitCallback(
            (long fromIndex, long toIndex) -> {

              // Convert from routing variable Index to user NodeIndex
              int fromNode = routingIndexManager.indexToNode(fromIndex);
              int toNode = routingIndexManager.indexToNode(toIndex);
              long traveltime =  timeMatrix[fromNode][toNode];
              return traveltime;
            }
    );
    routingModel.setArcCostEvaluatorOfAllVehicles(transitCallBackIndex);
    routingModel.addDimension(transitCallBackIndex, 10000, Integer.MAX_VALUE, false, "TravelTime");
    RoutingDimension dimension = routingModel.getDimensionOrDie("TravelTime");
//    dimension.setGlobalSpanCostCoefficient(100);
  }

  public void setDisjoint() {
    long penalty = 100000;
    for (int i =1; i < timeMatrix.length; i++) {
      routingModel.addDisjunction(new long[] {routingIndexManager.nodeToIndex(i)}, penalty);
    }
  }

  public void buildProblem() {
    routingIndexManager = new RoutingIndexManager(timeMatrix.length, vehicleNumber, depotIndex);
    routingModel = new RoutingModel(routingIndexManager);
    setCapacityConstrainsts();
    setTravelTimeMatrix();
    setTimeWindowConstraints();
    setDisjoint();
    // set service times
    // Or tools does not support separate service times, hence this is added to the travel time matrix.

    // minimise number of used vehicle
    routingModel.setFixedCostOfAllVehicles(100);
  }

  public void solve(RoutingSearchParameters searchParameters) {
    long start = System.currentTimeMillis();
    solution = routingModel.solveWithParameters(searchParameters);
    long end = System.currentTimeMillis();
    long size = routingModel.size();
    int status = routingModel.status();
    LOG.debug("Size " + size + " status  " + status);
    LOG.info("Duration: " + (end-start)/1000.0 + " seconds. Solver status " + status);
    printSolution(routingModel,routingIndexManager, solution );
  }

  public Assignment getSolution() {
    return solution;
  }

  public void printSolution(RoutingModel routingModel, RoutingIndexManager indexManager, Assignment solution) {
    RoutingDimension timeDimension = routingModel.getMutableDimension("TravelTime");
    long totalTime = 0;
    int usedVehicles = 0;
    long totalDistance = 0;
    int totalNumberOfVisitedDrops = 0;
    int totalNumberOfNodes = 0;
    Set<Integer> visitedNodes = new HashSet<>();
    for (int i = 0; i < vehicleNumber; i++) {
      long index = routingModel.start(i);
      LOG.debug("Route for vehicle " + i + ":");
      String route = "";
      while (!routingModel.isEnd(index)) {
        totalNumberOfVisitedDrops++;
        IntVar timeVar = timeDimension.cumulVar(index);
        route += indexManager.indexToNode(index) + "/" +
                " Time(" + solution.min(timeVar) + "," + solution.max(timeVar) + ": " +
                solution.value(timeVar)+ ") -> ";
        long previousIndex = index;
        index = solution.value(routingModel.nextVar(index));
        visitedNodes.add(Long.valueOf(index).intValue());
        totalDistance += routingModel.getArcCostForVehicle(previousIndex, index, i);
      }
      IntVar timeVar = timeDimension.cumulVar(index);
      route += indexManager.indexToNode(index) + " Time (" + solution.min(timeVar) + ","
              + solution.max(timeVar) + ")";
      LOG.debug(route);
      LOG.debug("Time of the route " + solution.min(timeVar) + " minutes.");
      totalTime += solution.min(timeVar);
      if (solution.min(timeVar) > 0) {
        usedVehicles++;
      }
    }
    LOG.debug("Total time of all routes: " + totalTime + " minutes. Used vehicles " +
            usedVehicles + " total distance " + totalDistance + " total number of drops " + totalNumberOfVisitedDrops +
            " number of disjoints " + routingModel.getNumberOfDisjunctions() + " set size " + visitedNodes.size());
  }

  public long[][] getTimeWindows() {
    return timeWindows;
  }

  public long[][] getTimeMatrix() {
    return timeMatrix;
  }

  public int getVehicleNumber() {
    return vehicleNumber;
  }

  public int getDepotIndex() {
    return depotIndex;
  }

  public SintefVehicle getVehicle() {
    return vehicle;
  }

  public long[] getVehicleCapacities() {
    return vehicleCapacities;
  }

  public long[] getDemands() {
    return demands;
  }

  public RoutingModel getRoutingModel() {
    return routingModel;
  }

  public RoutingIndexManager getRoutingIndexManager() {
    return routingIndexManager;
  }
}
