package app;

import com.google.ortools.constraintsolver.Assignment;
import common.optaplanner.basedomain.VehicleRoutingSolution;
import common.ortools.OrToolsProblem;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.math3.analysis.function.Sin;
import org.mvel2.sh.Main;
import org.optaplanner.core.api.score.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vrpproblems.ProblemType;
import vrpproblems.sintef.domain.SintefVehicleRoutingSolution;
import vrpproblems.sintef.solver.score.SintefEasyScoreCalculator;

import java.io.File;

public class MainApp {

//  private final static Logger LOG = Logger.getLogger(MainApp.class.getName());
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    Options options = getOptions();

    ProblemType problemType = null;
    File file = null;
    File outputFile;

    try {
      CommandLineParser commandLineParser = new DefaultParser();
      CommandLine cl = commandLineParser.parse(options, args);
      String runMode = cl.getOptionValue("m");
      file = new File(cl.getOptionValue("f"));
      problemType =  ProblemType.getProblemTypeByString(cl.getOptionValue("p"));
      outputFile = new File(cl.getOptionValue("o"));
      if (runMode.equalsIgnoreCase("optaplanner")) {
        LOG.info("Running optaplanner");
        OptaplannerApp optaplannerApp = new OptaplannerApp(problemType, file);
        VehicleRoutingSolution solvedSolution = optaplannerApp.run();
        solvedSolution.export(outputFile);
      } else if (runMode.equalsIgnoreCase("or-tools")) {
        LOG.info("Running or-tools");
        OrToolsApp orToolsApp = new OrToolsApp(problemType, file);
        VehicleRoutingSolution solvedSolution = orToolsApp.run();
        computeOptaplannerScore(solvedSolution);
        solvedSolution.export(outputFile);
      } else {
        throw new IllegalArgumentException("Run-mode of " + runMode + " is not recognised.");
      }

    } catch (ParseException p) {
      throw new RuntimeException("Could not parse the command line arguments");
    }

  }

  public static void computeOptaplannerScore(VehicleRoutingSolution problem) {
    if (problem instanceof  SintefVehicleRoutingSolution) {
      SintefVehicleRoutingSolution sintefProblem = (SintefVehicleRoutingSolution) problem;
      SintefEasyScoreCalculator sintefEasyScoreCalculator = new SintefEasyScoreCalculator();
      Score score = sintefEasyScoreCalculator.calculateScore(sintefProblem);
      System.out.println(score);
    }

  }


  public static Options getOptions() {
    Options options = new Options();
    options.addOption(Option.builder("m")
        .longOpt("run-mode")
        .desc("The mode to run the solver in. Accepts optaplanner or or-tools")
        .numberOfArgs(1)
        .hasArg()
        .required(true)
        .build());
    options.addOption(Option.builder("p")
        .longOpt("problem-type")
        .desc("The problem type to solve.")
        .numberOfArgs(1)
        .hasArg()
        .required(false)
        .build());
    options.addOption(Option.builder("f")
        .longOpt("file-path")
        .desc("File path for the problem to solve")
        .numberOfArgs(1)
        .hasArg()
        .required(false)
        .build());
    options.addOption(Option.builder("o")
        .longOpt("route-output-path")
        .desc("File path to save the solution results to")
        .numberOfArgs(1)
        .hasArg()
        .required(false)
        .build());
    options.addOption(Option.builder("id")
        .longOpt("input-directory")
        .desc("A directory containing the problems to be solved")
        .numberOfArgs(1)
        .hasArg()
        .required(false)
        .build());
    options.addOption(Option.builder("od")
        .longOpt("output-directory")
        .desc("Output directory containing the results of the problems to be solved")
        .numberOfArgs(1)
        .hasArg()
        .required(false)
        .build());
    return options;
  }
}
