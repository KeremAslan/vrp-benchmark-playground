package vrp.problems.sintef.domain;

import common.optaplanner.basedomain.DistanceType;
import common.optaplanner.basedomain.JobType;
import common.optaplanner.basedomain.XyCoordinates;
import org.junit.Assert;
import org.junit.Test;
import org.threeten.extra.Interval;
import vrpproblems.sintef.domain.SintefJob;
import vrpproblems.sintef.domain.SintefLocation;

import java.time.Instant;
import java.time.ZoneId;

public class SintefJobTests {


  final long SERVICE_TIME = 90;
  final int DEMAND = 10;
  final static ZoneId zoneId = ZoneId.of("UTC");


  @Test
  public void getDepartureTimeShouldReturnNullWhenRouteIsUnitialised() {

    SintefJob sintefJob = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );
    Assert.assertEquals(sintefJob.getArrivalTime(), null);
  }

  @Test
  public void getDepartureTimeShouldReturnServiceTimeWhenArrivalTimeIsZero() {

    SintefJob sintefJob = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
        );

    sintefJob.setArrivalTime(0L);
    Assert.assertEquals(sintefJob.getDepartureTime().longValue(), SERVICE_TIME);

  }

  @Test
  public void getDepartureTimeShouldReturnStartTimePlusServiceTimeWhenArrivalTimeIsBeforeTimeWindow() {
    SintefJob sintefJob = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(200), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );

    sintefJob.setArrivalTime(0L);

    Long departureTime = 200 + SERVICE_TIME;
    Assert.assertEquals(sintefJob.getDepartureTime(), departureTime);
  }

  @Test
  public void isArrivalOnTimeShouldReturnFalseWhenArrivalTimeIsSmallerThanTimeWindowStart() {
    SintefJob sintefJob = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(50), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );

    sintefJob.setArrivalTime(0L);

    Assert.assertFalse(sintefJob.isArrivalOnTime());

  }

  @Test
  public void isArrivalOnTimeShouldReturnFalseWhenArrivalTimeIsLargerThanTimeWindowEnd() {
    SintefJob sintefJob = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );

    sintefJob.setArrivalTime(1100L);

    Assert.assertFalse(sintefJob.isArrivalOnTime());

  }

  @Test
  public void isArrivalOnTimeShouldReturnTrueWhenArrivalTimeIsInInterval() {
    SintefJob sintefJob = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );

    sintefJob.setArrivalTime(500L);

    Assert.assertTrue(sintefJob.isArrivalOnTime());
  }

  @Test
  public void safeCloneShouldReturnIdenticalClone() {
    SintefJob sintefJob = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );

    SintefJob clonedJob = (SintefJob) sintefJob.safeClone();

    Assert.assertEquals(clonedJob.getId(), sintefJob.getId());
    Assert.assertEquals(clonedJob.getJobType(), sintefJob.getJobType());
    Assert.assertEquals(clonedJob.getLocation(), sintefJob.getLocation());
    Assert.assertEquals(clonedJob.getAllowedTimeWindow(), sintefJob.getAllowedTimeWindow());
    // previousStandstill is not cloned!
    Assert.assertEquals(clonedJob.getPreviousStandstill(), null);
    // nextJob is not cloned!
    Assert.assertEquals(clonedJob.getNextJob(), null);

  }

  public void interValShould() {
    SintefJob sintefJob1 = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );
  }

  @Test
  public void getDistanceFromPreviousStandstillShouldReturnSameDistanceAsGetDistanceTo() {
    SintefJob sintefJob1 = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );

    SintefJob sintefJob2 = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(100, 100)),
        Interval.of(Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );

    sintefJob1.setNextJob(sintefJob2);
    sintefJob2.setPreviousStandstill(sintefJob1);

    Long expected = sintefJob2.getTravelTimeInSecondsFromPreviousStandstill(DistanceType.STRAIGHT_LINE_DISTANCE);
    Long actual =  sintefJob2.getTravelTimeInSecondsTo(DistanceType.STRAIGHT_LINE_DISTANCE, sintefJob1);

    Assert.assertEquals(expected, actual);
  }


  @Test(expected = NullPointerException.class)
  public void getDistanceToDepotShouldThrowNullPointerExceptionWhenVehicleIsNotSet() {
    SintefJob sintefJob = new SintefJob(
        "Test customer",
        JobType.DROP_OFF,
        new SintefLocation("Test", new XyCoordinates(0, 0)),
        Interval.of(Instant.ofEpochSecond(0), Instant.ofEpochSecond(1000)),
        DEMAND,
        SERVICE_TIME,
        zoneId
    );

    sintefJob.getTravelTimeInSecondsToDepot(DistanceType.STRAIGHT_LINE_DISTANCE);

  }

}
