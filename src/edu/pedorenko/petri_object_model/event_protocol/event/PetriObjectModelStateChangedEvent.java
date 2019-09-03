package edu.pedorenko.petri_object_model.event_protocol.event;

import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectModelState;

public abstract class PetriObjectModelStateChangedEvent implements Event {

    private PetriObjectModelState petriObjectModelState;

    private double currentTime;

    public PetriObjectModelStateChangedEvent(PetriObjectModelState petriObjectModelState, double currentTime) {
        this.petriObjectModelState = petriObjectModelState;
        this.currentTime = currentTime;
    }

    public PetriObjectModelState getPetriObjectModelState() {
        return petriObjectModelState;
    }

    public double getCurrentTime() {
        return currentTime;
    }
}
