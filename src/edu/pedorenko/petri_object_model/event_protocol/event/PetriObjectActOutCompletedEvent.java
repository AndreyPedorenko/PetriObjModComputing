package edu.pedorenko.petri_object_model.event_protocol.event;

import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectState;

public class PetriObjectActOutCompletedEvent extends PetriObjectStateChangedEvent {

    public PetriObjectActOutCompletedEvent(PetriObjectState petriObjectState, double currentTime) {
        super(petriObjectState, currentTime);
    }
}
