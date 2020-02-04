package common.optaplanner.persistence;

import common.optaplanner.basedomain.VehicleRoutingSolution;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import java.io.File;

public class VehicleRoutingSolutionWriter implements SolutionFileIO<VehicleRoutingSolution> {

  @Override
  public String getInputFileExtension() {
    return null;
  }

  @Override
  public VehicleRoutingSolution read(File inputSolutionFile) {
    return null;
  }

  @Override
  public void write(VehicleRoutingSolution vehicleRoutingSolution, File outputSolutionFile) {

  }
}
