package edu.pedorenko.petri_object_model.event_protocol.event;

import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectState;

public abstract class PetriObjectStateChangedEvent implements Event {

    private PetriObjectState petriObjectState;

    private double currentTime;

    public PetriObjectStateChangedEvent(PetriObjectState petriObjectState, double currentTime) {
        this.petriObjectState = petriObjectState;
        this.currentTime = currentTime;
    }

    public PetriObjectState getPetriObjectState() {
        return petriObjectState;
    }

    public double getCurrentTime() {
        return currentTime;
    }
}
