package common.basedomain;


import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public interface Standstill {


  @InverseRelationShadowVariable(sourceVariableName = "previousStandstill")
  Job getNextJob();

  void setNextJob(Job nextStandstill);

  Location getLocation();

  Long getTravelTimeInSecondsTo(DistanceType distanceType, Standstill standstillToTravelTo);

  Vehicle getVehicle();


  String getId();

}
