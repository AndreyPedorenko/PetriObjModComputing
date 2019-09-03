package edu.pedorenko.petri_object_model.event_protocol.state_model;

import java.util.List;

public class PetriObjectModelState {

    private List<PetriObjectState> petriObjectsState;

    public PetriObjectModelState(List<PetriObjectState> petriObjectsState) {
        this.petriObjectsState = petriObjectsState;
    }

    public List<PetriObjectState> getPetriObjectsState() {
        return petriObjectsState;
    }
}
