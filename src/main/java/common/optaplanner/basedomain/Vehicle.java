package common.optaplanner.basedomain;


import common.optaplanner.persistence.dto.Dto;
import common.optaplanner.solver.NumberOfJobsUpdatingVariableListener;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

@PlanningEntity
public abstract class Vehicle implements Standstill {

  private String id;
  private VehicleType vehicleType;

  private Depot startDepot;
  private Depot endDepot;

  //Shadow variables
  private Integer numberOfJobs;
  public final static String NUMBER_OF_JOBS_VARIABLE_NAME = "numberOfJobs";
  private Job nextJob;

  public Vehicle(String id, VehicleType vehicleType, Depot startDepot, Depot endDepot) {
    this.id = id;
    this.vehicleType = vehicleType;
    this.startDepot = startDepot;
    this.endDepot = endDepot;
    this.numberOfJobs = 0;
  }

  @Override
  @PlanningId
  public String getId() {
    return id;
  }


  public VehicleType getVehicleType() {
    return vehicleType;
  }

  public abstract Vehicle safeClone();

  public void setStartDepot(Depot startDepot) {
    this.startDepot = startDepot;
  }

  public void setEndDepot(Depot endDepot) {
    this.endDepot = endDepot;
  }

  public Depot getStartDepot() {
    return startDepot;
  }

  public Depot getEndDepot() {
    return endDepot;
  }

  @Override
  public Location getLocation() {
    return startDepot.getLocation();
  }

  @Override
  public String toString() {
    return "Vehicle{" +
        "id='" + id + '\'' +
        ", vehicleType=" + vehicleType +
        '}';
  }

  @Override
  public Job getNextJob() {
    return nextJob;
  }

  @Override
  public void setNextJob(Job nextJob) {
    this.nextJob = nextJob;
  }

  @CustomShadowVariable(variableListenerClass = NumberOfJobsUpdatingVariableListener.class,
  sources = {@PlanningVariableReference(entityClass = common.optaplanner.basedomain.Standstill.class, variableName = "nextJob")})
  public Integer getNumberOfJobs() {
    return numberOfJobs;
  }

  public void setNumberOfJobs(int numberOfJobs) {
    this.numberOfJobs = numberOfJobs;
  }

  public abstract Dto convertToDto();
}
