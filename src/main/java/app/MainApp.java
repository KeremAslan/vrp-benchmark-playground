package app;

import common.optaplanner.basedomain.VehicleRoutingSolution;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.sh.Main;
import org.optaplanner.core.api.score.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vrpproblems.ProblemType;
import vrpproblems.sintef.domain.SintefVehicleRoutingSolution;
import vrpproblems.sintef.solver.score.SintefEasyScoreCalculator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MainApp {

//  private final static Logger LOG = Logger.getLogger(MainApp.class.getName());
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    Options options = getOptions();

    ProblemType problemType = null;
    String outputFile;
    String runtimeInMinutes;

    try {
      CommandLineParser commandLineParser = new DefaultParser();
      CommandLine cl = commandLineParser.parse(options, args);
      String runMode = cl.getOptionValue("m");
      runtimeInMinutes = cl.getOptionValue("runtimeInMinutes");
      problemType =  ProblemType.getProblemTypeByString(cl.getOptionValue("p"));

      if (runMode.equalsIgnoreCase("benchmark")) {
        String folderToBenchmark = cl.getOptionValue("f");
        File folder = new File(folderToBenchmark);
        Map<String, Score> optaplannerScores = new HashMap<>();
        Map<String, Score> ortoolsScores = new HashMap<>();
        for(File file : folder.listFiles()) {
          Score optaplannerScore = runOptaplanner(file.getPath(), problemType, runtimeInMinutes, "");
          Score ortoolsScore = runOrTools(file.getPath(), problemType, runtimeInMinutes, "");
          optaplannerScores.put(file.getName(), optaplannerScore);
          ortoolsScores.put(file.getName(), ortoolsScore);
          break;
        }
        writeResultsToCsv(optaplannerScores, ortoolsScores, "output.csv");
      } else {
        String pathToFile = cl.getOptionValue("f");
        outputFile = cl.getOptionValue("o");

        if (runMode.equalsIgnoreCase("optaplanner")) {
          LOG.info("Running optaplanner");
          runOptaplanner(pathToFile, problemType, runtimeInMinutes, outputFile);
        } else if (runMode.equalsIgnoreCase("or-tools")) {
          LOG.info("Running or-tools");
          runOrTools(pathToFile, problemType, runtimeInMinutes, outputFile);
        } else {
          throw new IllegalArgumentException("Run-mode of " + runMode + " is not recognised.");
        }
      }
    } catch (ParseException p) {
      throw new RuntimeException("Could not parse the command line arguments");
    }

  }

  public static void writeResultsToCsv(Map<String, Score> ortoolsScore, Map<String, Score> optplannerScore, String filePath) {
    List<List<String>> rows = new ArrayList<>();
    rows.addAll(extractRowsFromMap(ortoolsScore));
    rows.addAll(extractRowsFromMap(optplannerScore));
    try {
      FileWriter csvWriter = new FileWriter(filePath);

      csvWriter.append("Problem")
              .append(",").append("unassignedjobs")
              .append(",").append("violations")
              .append(",").append("noVehicles")
              .append(",").append("totalDistance")
              .append("\n");

      for (List<String> row : rows) {
        csvWriter.append(String.join(",", row));
        csvWriter.append("\n");
      }
      csvWriter.flush();
      csvWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static List<List<String>> extractRowsFromMap(Map<String, Score> scoreMap) {
    List<List<String>> extractedRows = new ArrayList<>();
    if (scoreMap != null) {
      for (Map.Entry<String, Score> entry : scoreMap.entrySet()) {
        String score = entry.getValue().toString();
        score = StringUtils.remove(score, "[" );
        score = StringUtils.remove(score, "]");
        score = StringUtils.remove(score, "hard");
        score = StringUtils.remove(score, "soft");
        String[] splittedScore = StringUtils.split(score, "/");

        String unassignedJobs = splittedScore[0];
        String violations =   splittedScore[1];
        String noVehicles =  splittedScore[2];
        String totalDistance =  splittedScore[3];
        List<String> row = Arrays.asList(entry.getKey(), unassignedJobs, violations, noVehicles, totalDistance);
        extractedRows.add(row);
      }
    }
    return extractedRows;
  }

  public static Score runOptaplanner(String path, ProblemType problemType, String runtimeInMinutes, String outputPath) {
    File file = new File(path);
    OptaplannerApp optaplannerApp = new OptaplannerApp(problemType, file);
    VehicleRoutingSolution solvedSolution = optaplannerApp.run(Integer.valueOf(runtimeInMinutes));
//    solvedSolution.export(new File(outputPath));
    return solvedSolution.getScore();
  }

  public static Score runOrTools(String path, ProblemType problemType, String runtimeInMinutes, String outputPath) {
    File file = new File(path);
    OrToolsApp orToolsApp = new OrToolsApp(problemType, file);
    VehicleRoutingSolution solvedSolution = orToolsApp.run(Integer.valueOf(runtimeInMinutes));
    Score score = computeOptaplannerScore(solvedSolution);
//    solvedSolution.export(new File(outputPath));
    return score;
  }

  public static Score computeOptaplannerScore(VehicleRoutingSolution problem) {
    if (problem instanceof  SintefVehicleRoutingSolution) {
      SintefVehicleRoutingSolution sintefProblem = (SintefVehicleRoutingSolution) problem;
      SintefEasyScoreCalculator sintefEasyScoreCalculator = new SintefEasyScoreCalculator();
      return sintefEasyScoreCalculator.calculateScore(sintefProblem);
    }
    throw new RuntimeException("Something went wrong when trying to compute Optaplanner score for ortools solution");

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
    options.addOption(Option.builder("rt")
            .longOpt("runtimeInMinutes")
            .desc("The runtime in minutes for the problem")
            .numberOfArgs(1)
            .hasArg()
            .required(false)
            .build());
    return options;
  }
}
