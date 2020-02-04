package vrp.testhelpers;

import common.optaplanner.basedomain.*;
import org.apache.commons.lang3.NotImplementedException;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import vrpproblems.ProblemType;
import vrpproblems.sintef.domain.SintefShift;
import vrpproblems.sintef.domain.SintefVehicleRoutingSolution;
import vrpproblems.sintef.persistence.SintefReader;

import java.io.InputStream;
import java.util.List;

public class OptaplannerHelper {

  public static VehicleRoutingSolution buildOptaplannerProblem(ProblemType problemType, int numberOfVehicles, int numberOfJobs){
    switch(problemType) {
      case SINTEF:
        return buildSintefOptaplannerProblem(numberOfVehicles, numberOfJobs);
      default:
        throw new NotImplementedException("Cannot build Optaplanner problem of type " + problemType + " as this is not yet implemented.");
    }
  }


  public static VehicleRoutingSolution buildSintefOptaplannerProblem(int numberOfVehicles, int numberOfJobs) {
    SintefReader sintefReader = new SintefReader();
    InputStream problemFile = OptaplannerHelper.class.getClassLoader().getResourceAsStream("common/vrp/problems/sintef/data/C1_10_1.TXT");
    VehicleRoutingSolution problem = sintefReader.read(problemFile);
    List<Job> jobs = problem.getJobs();
    List<Vehicle> vehicles = problem.getVehicles();
    List<Location> locations = problem.getLocations();

    if (numberOfVehicles > vehicles.size() || numberOfJobs > jobs.size())
      throw new IndexOutOfBoundsException(
          "numberOfVehicles and numberOfJobs cannot be more than " + 250 + " and " + 1000 + " respectively." +
              "Received: " + numberOfJobs + " for numberOfJobs and " + numberOfVehicles + " numberOfVehicles.");

    vehicles = vehicles.subList(0, numberOfVehicles);
    jobs = jobs.subList(0, numberOfJobs);

    Shift shift = new SintefShift("Test Shift", vehicles, jobs, locations);
    problem = new SintefVehicleRoutingSolution(shift);

    return problem;

  }

  public static VehicleRoutingSolution runOptaplannerWithBasicConfiguration(ProblemType problemType, VehicleRoutingSolution problem) {
    SolverFactory<VehicleRoutingSolution> solverFactory = SolverFactory.createFromXmlResource("common/vrp/problems/sintef/sintef-solver-only-construction-configuration.xml");
    Solver<VehicleRoutingSolution> solver = solverFactory.buildSolver();
    VehicleRoutingSolution solvedSolution = solver.solve(problem);
    return solvedSolution;
  }






}
