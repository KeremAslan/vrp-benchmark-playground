package app;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;

import java.util.logging.Logger;

public class Benchmarker {

  /** Default logger for this class */
  private static final Logger LOG = Logger.getLogger(Benchmarker.class.getName());

  public static void main(String[] args) {
    runBenchmark(args[0]);
  }

  public static void runBenchmark(String pathToConfigFile) {

    LOG.info("Running benchmarker with the configuration file " + pathToConfigFile );

    PlannerBenchmarkFactory plannerBenchmarkFactory;
    if (pathToConfigFile.endsWith("ftl")) {
      plannerBenchmarkFactory = PlannerBenchmarkFactory.createFromFreemarkerXmlResource(pathToConfigFile);
    } else {
      plannerBenchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource(pathToConfigFile);
    }

    PlannerBenchmark plannerBenchmark = plannerBenchmarkFactory.buildPlannerBenchmark();
    plannerBenchmark.benchmark();

  }

}
