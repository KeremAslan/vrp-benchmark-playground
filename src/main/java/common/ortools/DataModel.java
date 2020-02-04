package common.ortools;

import common.optaplanner.basedomain.DistanceType;
import common.optaplanner.basedomain.Location;

import java.util.List;

public class DataModel {

    public long[][] distanceMatrix = null;

    public DataModel(List<Location> locations) {
        parseLocations(locations);
    }

    private void parseLocations(List<Location> locations) {
        distanceMatrix = new long[locations.size()][locations.size()];
        for (int i=0; i < locations.size(); i++) {
            for (int j=0; j < locations.size(); j++) {
                Location location1 = locations.get(i);
                Location location2 = locations.get(j);
                distanceMatrix[i][j] = location1.getDistanceTo(DistanceType.STRAIGHT_LINE_DISTANCE, location2);
            }
        }
    }
}
