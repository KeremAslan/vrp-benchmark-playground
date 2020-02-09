package app;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;
import common.optaplanner.basedomain.VehicleRoutingSolution;
import common.ortools.OrToolsProblem;
import common.ortools.persistence.OptaplannerToOrToolsAdapter;
import vrpproblems.ProblemType;
import vrpproblems.sintef.persistence.SintefReader;

import java.io.File;
import java.util.logging.Logger;

public class OrToolsApp {

  static {
    System.loadLibrary("jniortools");
  }

  private static final Logger LOG = Logger.getLogger(OrToolsApp.class.getName());

  private ProblemType problemType;
  private OrToolsProblem uninitiatedVrpProblem;
  private Assignment solvedSolution;

  public OrToolsApp(ProblemType problemType, File file) {
    this.problemType = problemType;
    init(file);
  }

  private void init(File file) {
    switch (problemType) {
      case SINTEF:
        SintefReader sintefReader = new SintefReader();
        VehicleRoutingSolution optaplannerModel = sintefReader.read(file); //Optaplanner model;
        OptaplannerToOrToolsAdapter adapter = new OptaplannerToOrToolsAdapter(optaplannerModel);
        uninitiatedVrpProblem = adapter.getOrToolsProblem();
        break;
      default:
        uninitiatedVrpProblem = null;
        LOG.warning("uniniatedVrpProblem is not set because the problem type couldn't be recognized");
    }
  }

  public Assignment run() {
    if (uninitiatedVrpProblem == null || problemType == null)  {
      throw new NullPointerException("The variable uninitiatedVrpProblem is not yet initialised. Call OptaplannerApp.init() first to initialise a solution");
    }

    uninitiatedVrpProblem.buildProblem();

    RoutingSearchParameters searchParameters = main
        .defaultRoutingSearchParameters()
        .toBuilder()
        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
        .build();
    uninitiatedVrpProblem.solve(searchParameters);
    this.solvedSolution = uninitiatedVrpProblem.getSolution();
    return solvedSolution;
  }

}
