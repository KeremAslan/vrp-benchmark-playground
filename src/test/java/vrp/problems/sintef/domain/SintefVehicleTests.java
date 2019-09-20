package vrp.problems.sintef.domain;



import common.basedomain.DistanceType;
import common.basedomain.JobType;
import common.basedomain.Location;
import common.basedomain.VehicleType;
import common.basedomain.XyCoordinates;
import org.junit.Assert;
import org.junit.Test;
import org.threeten.extra.Interval;
import vrpproblems.sintef.domain.SintefDepot;
import vrpproblems.sintef.domain.SintefJob;
import vrpproblems.sintef.domain.SintefLocation;
import vrpproblems.sintef.domain.SintefVehicle;

import java.time.Instant;
import java.time.ZoneId;

public class SintefVehicleTests {

  private final SintefVehicle sintefVehicle;

  private final static ZoneId ZONE_ID = ZoneId.of("UTC");


  public SintefVehicleTests() {
    // These objects are created based on Sintef Dataset C1_10_2
    int capacity = 200;
    Location sintefDepotLocation = new SintefLocation("Depot location", new XyCoordinates(250, 250));
    SintefDepot sintefDepot = new SintefDepot(
        sintefDepotLocation, "Depot", Interval.of(Instant.ofEpochMilli(00), Instant.ofEpochMilli(1824)), ZONE_ID);
    this.sintefVehicle = new SintefVehicle("Test Vehicle", VehicleType.VAN,  sintefDepot, sintefDepot, sintefDepot.getOperationalTimeWindow(), capacity);
  }

  @Test
  public void getNextStandstillShouldReturnNullWhenVehicleIsUnassigned() {
    Assert.assertEquals(null, sintefVehicle.getNextJob());
  }

  @Test
  public void getVehicleShouldReturnSelf() {
    Assert.assertEquals(sintefVehicle, sintefVehicle.getVehicle());
  }

  @Test
  public void getDistanceToShouldReturnOneHundredFortyFiveWhenDistanceToIsFirstCustomer() {
    SintefJob sintefJob = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(387, 297)),
        Interval.of(Instant.ofEpochMilli(200), Instant.ofEpochMilli(270)),
        10,
        90L,
        ZONE_ID
    );
    Assert.assertEquals(
        sintefVehicle.getTravelTimeInSecondsTo(DistanceType.STRAIGHT_LINE_DISTANCE, sintefJob), Long.valueOf(145));
  }

  @Test
  public void getDistanceToShouldReturnZeroWhenDistanceToIsSelf() {
    Assert.assertEquals(
        sintefVehicle.getTravelTimeInSecondsTo(DistanceType.STRAIGHT_LINE_DISTANCE, sintefVehicle), Long.valueOf(0));
  }

  @Test
  public void safeCloneShouldReturnIdenticalClonedVehicle() {
    SintefVehicle clonedVehicle = (SintefVehicle) sintefVehicle.safeClone();
    Assert.assertEquals(clonedVehicle.getId(), sintefVehicle.getId());
    Assert.assertEquals(clonedVehicle.getStartDepot(), sintefVehicle.getStartDepot());
    Assert.assertEquals(clonedVehicle.getEndDepot(), sintefVehicle.getEndDepot());
    Assert.assertEquals(clonedVehicle.getCapacity(), sintefVehicle.getCapacity());
    Assert.assertEquals(clonedVehicle.getOperationalTimeWindow(), sintefVehicle.getOperationalTimeWindow());
    // next Standstill is not cloned in the clone method.
    Assert.assertEquals(clonedVehicle.getNextJob(), sintefVehicle.getNextJob());
  }
}
