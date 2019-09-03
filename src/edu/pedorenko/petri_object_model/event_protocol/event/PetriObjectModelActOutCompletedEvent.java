package edu.pedorenko.petri_object_model.event_protocol.event;

import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectModelState;

public class PetriObjectModelActOutCompletedEvent extends PetriObjectModelStateChangedEvent {

    public PetriObjectModelActOutCompletedEvent(PetriObjectModelState petriObjectModelState, double currentTime) {
        super(petriObjectModelState, currentTime);
    }
}
