package common.optaplanner.persistence;

import java.io.File;

/**
 * A common interface for all problem readers
 */
public interface ProblemReader<VehicleRoutingSolution> {

  VehicleRoutingSolution read(File file);
}
