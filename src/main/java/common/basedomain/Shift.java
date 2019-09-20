package common.basedomain;


import java.util.List;

public abstract class Shift {

  private String id;

  private List<Vehicle> vehicles;
  private List<Job> jobs;

  public Shift(String id, List<Vehicle> vehicles, List<Job> jobs) {
    this.id = id;
    this.vehicles = vehicles;
    this.jobs = jobs;
  }

  public String getId() {
    return id;
  }

  public List<Vehicle> getVehicles() {
    return vehicles;
  }

  public List<Job> getJobs() {
    return jobs;
  }

  @Override
  public String toString() {
    return "Shift{" +
        "id='" + id + '\'' +
        ", no_vehicles=" + vehicles.size() +
        ", no_jobs=" + jobs.size() +
        '}';
  }

  public abstract Shift safeClone();

}
