package app;

import common.basedomain.VehicleRoutingSolution;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.mvel2.sh.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vrpproblems.ProblemType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
      Integer runMode = Integer.valueOf(cl.getOptionValue("m"));
      if (runMode == 1) {
        // single file mode
        LOG.info("Running solver in mode 1 (single file mode)");
        problemType =  ProblemType.getProblemTypeByString(cl.getOptionValue("p"));

        file = new File(cl.getOptionValue("f"));
        outputFile = new File(cl.getOptionValue("o"));

        App app = new App(problemType, file);
        VehicleRoutingSolution solvedSolution = app.run();
        solvedSolution.export(outputFile);
      } else if (runMode == 2) {
        LOG.info("Running solver in mode 2 (batch mode)");
      } else {
        throw new IllegalArgumentException("Unknown value for runmode " + runMode);
      }

    } catch (ParseException p) {
      throw new RuntimeException("Could not parse the command line arguments");
    }

  }



  public static Options getOptions() {
    Options options = new Options();
    options.addOption(Option.builder("m")
        .longOpt("run-mode")
        .desc("The mode to run the solver in. Takes 1 for running taking a single file or 2 to run a batch of files in a folder")
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
