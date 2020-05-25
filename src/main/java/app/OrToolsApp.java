package app;

import com.google.ortools.constraintsolver.*;
import com.google.protobuf.Duration;
import common.optaplanner.basedomain.VehicleRoutingSolution;
import common.ortools.OrToolsProblem;
import common.ortools.persistence.OptaplannerToOrToolsAdapter;
import common.ortools.persistence.OrToolsToOptaplannerAdapter;
import vrpproblems.ProblemType;
import vrpproblems.sintef.persistence.SintefReader;

import java.io.File;
import java.util.logging.Logger;

public class OrToolsApp {

  static {
    File file = new File("src/main/resources/lib/jniortools.dll");
    System.load(file.getAbsolutePath());
//    System.loadLibrary("jniortools");
  }

  private static final Logger LOG = Logger.getLogger(OrToolsApp.class.getName());

  private ProblemType problemType;
  private OrToolsProblem uninitiatedVrpProblem;
  private Assignment solvedSolution;
  private VehicleRoutingSolution optaplannerModel;

  public OrToolsApp(ProblemType problemType, File file) {
    this.problemType = problemType;
    init(file);
  }

  private void init(File file) {
    switch (problemType) {
      case SINTEF:
        SintefReader sintefReader = new SintefReader();
        optaplannerModel = sintefReader.read(file); //Optaplanner model;
        OptaplannerToOrToolsAdapter adapter = new OptaplannerToOrToolsAdapter(optaplannerModel);
        uninitiatedVrpProblem = adapter.getOrToolsProblem();
        break;
      default:
        uninitiatedVrpProblem = null;
        LOG.warning("uniniatedVrpProblem is not set because the problem type couldn't be recognized");
    }
  }

  public VehicleRoutingSolution run(Integer runTimeInMinutes) {
    if (uninitiatedVrpProblem == null || problemType == null)  {
      throw new NullPointerException("The variable uninitiatedVrpProblem is not yet initialised. Call OptaplannerApp.init() first to initialise a solution");
    }
    if (runTimeInMinutes == null) {
      runTimeInMinutes = 1;
    }
    uninitiatedVrpProblem.buildProblem();
    RoutingSearchParameters searchParameters = main
        .defaultRoutingSearchParameters()
        .toBuilder()
        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.AUTOMATIC)
        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
        .setTimeLimit(Duration.newBuilder().setSeconds(runTimeInMinutes* 60).build())
//        .setLogSearch(true)
//        .setSolutionLimit(100)
        .build();

    uninitiatedVrpProblem.solve(searchParameters);
    this.solvedSolution = uninitiatedVrpProblem.getSolution();
    OrToolsToOptaplannerAdapter orToolsToOptaplannerAdapter = new OrToolsToOptaplannerAdapter(
            solvedSolution,
            uninitiatedVrpProblem.getRoutingIndexManager(),
            uninitiatedVrpProblem.getRoutingModel(),
            optaplannerModel);

    return orToolsToOptaplannerAdapter.convert();
  }


}
