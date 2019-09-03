package edu.pedorenko.petri_object_model.statistics;

import edu.pedorenko.petri_object_model.computing_model.petri_object.place.ComputingPlace;

public class PlaceStatistics {

    private ComputingPlace computingPlace;

    private long marking;

    private long maxObservedMarking = Long.MIN_VALUE;

    private long minObservedMarking = Long.MAX_VALUE;

    private double meanMarking;

    public PlaceStatistics(ComputingPlace computingPlace) {
        this.computingPlace = computingPlace;
    }

    void doStatistics(double timeDelta) {
        marking = computingPlace.getMarking();

        if (marking > maxObservedMarking) {
            maxObservedMarking = marking;
        }

        if (marking < minObservedMarking) {
            minObservedMarking = marking;
        }

        meanMarking = meanMarking + (marking - meanMarking) * timeDelta;
    }

    public long getMarking() {
        return marking;
    }

    public long getMaxObservedMarking() {
        return maxObservedMarking;
    }

    public long getMinObservedMarking() {
        return minObservedMarking;
    }

    public double getMeanMarking() {
        return meanMarking;
    }

    public String toString() {
        return "PlaceStatistics{" +
                "marking=" + marking +
                ", maxObservedMarking=" + maxObservedMarking +
                ", minObservedMarking=" + minObservedMarking +
                ", meanMarking=" + meanMarking +
                '}';
    }
}
