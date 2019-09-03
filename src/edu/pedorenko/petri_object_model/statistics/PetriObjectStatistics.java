package edu.pedorenko.petri_object_model.statistics;

import java.util.HashMap;
import java.util.Map;

public class PetriObjectStatistics {

    private Map<Long, TransitionStatistics> transitionsStatistics = new HashMap<>();

    private Map<Long, PlaceStatistics> placesStatistics = new HashMap<>();

    public PetriObjectStatistics() {
    }

    public TransitionStatistics getTransitionStatisticsByTransitionId(long transitionId) {
        return transitionsStatistics.get(transitionId);
    }

    public PlaceStatistics getPlaceStatisticsByPlaceId(long placeId) {
        return placesStatistics.get(placeId);
    }

    public void putTransitionStatistics(long transitionId, TransitionStatistics transitionStatistics) {
        transitionsStatistics.put(transitionId, transitionStatistics);
    }

    public void putPlaceStatistics(long placeId, PlaceStatistics placeStatistics) {
        placesStatistics.put(placeId, placeStatistics);
    }

    public boolean hasStatistics() {
        return !transitionsStatistics.isEmpty() || !placesStatistics.isEmpty();
    }

    public void doStatistics(double timeDelta) {
        for (TransitionStatistics transitionStatistics : transitionsStatistics.values()) {
            transitionStatistics.doStatistics(timeDelta);
        }

        for (PlaceStatistics placeStatistics : placesStatistics.values()) {
            placeStatistics.doStatistics(timeDelta);
        }
    }

    public String toString() {
        return "PetriObjectStatistics{" +
                "transitionsStatistics=" + transitionsStatistics +
                ", placesStatistics=" + placesStatistics +
                '}';
    }
}
