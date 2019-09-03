package edu.pedorenko.petri_object_model.event_protocol.event;

import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectState;

public class PetriObjectActInCompletedEvent extends PetriObjectStateChangedEvent {
    public PetriObjectActInCompletedEvent(PetriObjectState petriObjectState, double currentTime) {
        super(petriObjectState, currentTime);
    }
}
