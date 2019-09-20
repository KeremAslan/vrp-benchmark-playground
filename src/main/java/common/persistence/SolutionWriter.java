package common.persistence;


import java.io.File;

public interface SolutionWriter<VehicleRoutingSolution> {

  void write(VehicleRoutingSolution vehicleRoutingSolution, File file);
}
