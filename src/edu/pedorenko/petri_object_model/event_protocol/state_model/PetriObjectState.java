package edu.pedorenko.petri_object_model.event_protocol.state_model;

import java.util.List;

public class PetriObjectState {

    private PetriObjectCharacteristics petriObjectCharacteristics;

    private List<TransitionState> transitionsState;
    private List<PlaceState> placesState;

    private double nearestEventTime;

    public PetriObjectState(
            PetriObjectCharacteristics petriObjectCharacteristics,
            List<TransitionState> transitionsState,
            List<PlaceState> placesState,
            double nearestEventTime) {
        this.petriObjectCharacteristics = petriObjectCharacteristics;
        this.transitionsState = transitionsState;
        this.placesState = placesState;
        this.nearestEventTime = nearestEventTime;
    }

    public PetriObjectCharacteristics getPetriObjectCharacteristics() {
        return petriObjectCharacteristics;
    }

    public List<TransitionState> getTransitionsState() {
        return transitionsState;
    }

    public List<PlaceState> getPlacesState() {
        return placesState;
    }

    public double getNearestEventTime() {
        return nearestEventTime;
    }
}
