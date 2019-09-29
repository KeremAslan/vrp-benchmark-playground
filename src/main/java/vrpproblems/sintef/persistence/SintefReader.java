package vrpproblems.sintef.persistence;

import common.basedomain.Job;
import common.basedomain.JobType;
import common.basedomain.Location;
import common.basedomain.Shift;
import common.basedomain.Vehicle;
import common.basedomain.VehicleRoutingSolution;
import common.basedomain.VehicleType;
import common.basedomain.XyCoordinates;
import common.persistence.ProblemReader;
import org.apache.commons.lang3.StringUtils;
import org.threeten.extra.Interval;
import vrpproblems.sintef.domain.SintefDepot;
import vrpproblems.sintef.domain.SintefJob;
import vrpproblems.sintef.domain.SintefLocation;
import vrpproblems.sintef.domain.SintefShift;
import vrpproblems.sintef.domain.SintefVehicle;
import vrpproblems.sintef.domain.SintefVehicleRoutingSolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SintefReader implements ProblemReader<VehicleRoutingSolution> {

  /** Default logger for this class */
  private static final Logger LOG = Logger.getLogger(SintefReader.class.getName());

  private final static int CUSTOMER_NO = 0;
  private final static int X_COORD = 1;
  private final static int Y_COORD = 2;
  private final static int DEMAND = 3;
  private final static int READY_TIME = 4;
  private final static int DUE_TIME = 5;
  private final static int SERVICE_TIME = 6;

  private final static int DEPOT_LINE = 10;
  private final static int VEHICLES_LINE = 5;
  private final static int NUMBER_OF_VEHICLES = 0;
  private final static int VEHICLE_CAPACITY = 1;


  @Override
  public VehicleRoutingSolution read(File file) {
    try {
      InputStream stream = new FileInputStream(file);
      return read(stream);
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex);
    }
  }

  public VehicleRoutingSolution read(InputStream stream) {
    List<Location> locationList = new ArrayList<>();
    List<Vehicle> vehicleList = new ArrayList<>();
    List<Job> jobList = new ArrayList<>();
    SintefDepot depot = null;

    try {
      BufferedReader br = new BufferedReader( new InputStreamReader(stream));
      String line;

      int lineNumber = 1;

      int customerCount = 0;

      int numberOfVehicles = 0;
      int vehicleCapacity = 0;
      while ( (line = br.readLine()) != null && !line.contains("EOF")) {

        if (lineNumber == VEHICLES_LINE) {
          String[] row = StringUtils.split(StringUtils.trim(line));

          numberOfVehicles = Integer.parseInt(row[NUMBER_OF_VEHICLES]);
          vehicleCapacity = Integer.parseInt(row[VEHICLE_CAPACITY]);


        } else if (lineNumber >= DEPOT_LINE){
          String[] row = StringUtils.split(StringUtils.trim(line));
          String id = row[CUSTOMER_NO];
          double xCoord = Double.parseDouble(row[X_COORD]);
          double yCoord = Double.parseDouble(row[Y_COORD]);
          int demand = Integer.parseInt(row[DEMAND]);
          int readyTime = Integer.parseInt(row[READY_TIME]);
          int dueTime = Integer.parseInt(row[DUE_TIME]);
          int serviceTime = Integer.parseInt(row[SERVICE_TIME]);

          Location location = new SintefLocation(id, new XyCoordinates(xCoord, yCoord));
          if (lineNumber == DEPOT_LINE ) {
            Interval timeWindow = Interval.of(Instant.ofEpochSecond(readyTime), Instant.ofEpochSecond(dueTime));
            depot = new SintefDepot(location, "SintefDepot", timeWindow, ZoneId.of("UTC"));
          } else if (lineNumber > DEPOT_LINE) {
            customerCount++;
            Interval timeWindow = Interval.of(Instant.ofEpochSecond(readyTime), Instant.ofEpochSecond(dueTime));
            Job sintefJob = new SintefJob(String.valueOf(customerCount), JobType.DROP_OFF, location, timeWindow, demand, serviceTime, ZoneId.of("UTC"));
            jobList.add(sintefJob);
            locationList.add(location);
          }
        }
        lineNumber++;
      }

      for (int i=0; i<numberOfVehicles; i++) {
        Vehicle vehicle = new SintefVehicle(String.valueOf(i), VehicleType.VAN , depot, depot,  depot.getOperationalTimeWindow(), vehicleCapacity);
        vehicleList.add(vehicle);
      }
    } catch (IOException  ex ) {
      LOG.warning("Could not parse file due to the following exception: " + ex);
      throw new RuntimeException("Failed to parse input file");
    }

    Shift shift = new SintefShift("SintefShift", vehicleList, jobList, locationList);
    VehicleRoutingSolution solution = new SintefVehicleRoutingSolution(shift);

    return solution;
  }
}
