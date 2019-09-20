package app;

import common.basedomain.VehicleRoutingSolution;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import vrpproblems.ProblemType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class MainApp {

  private final static Logger LOG = Logger.getLogger(MainApp.class.getName());

  public static void main(String[] args) {

    Options options = getOptions();

    ProblemType problemType = null;
    File file = null;
    File outputFile;

    try {
      CommandLineParser commandLineParser = new DefaultParser();
      CommandLine cl = commandLineParser.parse(options, args);

      if (cl.getOptionValue("m").equalsIgnoreCase(String.valueOf(1))) {
        LOG.info("Running solver in mode 1");
        problemType =  ProblemType.getProblemTypeByString(cl.getOptionValue("p"));

        file = new File(cl.getOptionValue("f"));
        outputFile = new File(cl.getOptionValue("o"));

        App app = new App(problemType, file);
        VehicleRoutingSolution solvedSolution = app.run();
        solvedSolution.export(outputFile);
      }

    } catch (ParseException p) {
      throw new RuntimeException("Could not parse the command line arguments");
    }

  }



  public static Options getOptions() {
    Options options = new Options();
    options.addOption(Option.builder("m")
        .longOpt("run-mode")
        .desc("The mode oto run the solver in.")
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
