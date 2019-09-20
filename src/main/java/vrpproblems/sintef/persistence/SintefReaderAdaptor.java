package vrpproblems.sintef.persistence;

import common.basedomain.VehicleRoutingSolution;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import java.io.File;

public class SintefReaderAdaptor implements SolutionFileIO<VehicleRoutingSolution> {


  @Override
  public String getInputFileExtension() {
    return null;
  }

  @Override
  public VehicleRoutingSolution read(File file) {
    SintefReader reader = new SintefReader();
    return reader.read(file);
  }

  @Override
  public void write(VehicleRoutingSolution vrpSolution, File file) {

  }
}
