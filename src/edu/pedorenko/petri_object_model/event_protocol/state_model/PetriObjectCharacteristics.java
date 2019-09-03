package edu.pedorenko.petri_object_model.event_protocol.state_model;

import java.util.List;

public class PetriObjectCharacteristics {

    private List<Long> petriObjectIds;

    private String petriObjectName;

    private int priority;

    public PetriObjectCharacteristics(List<Long> petriObjectIds, String petriObjectName, int priority) {
        this.petriObjectIds = petriObjectIds;
        this.petriObjectName = petriObjectName;
        this.priority = priority;
    }

    public List<Long> getPetriObjectIds() {
        return petriObjectIds;
    }

    public String getPetriObjectName() {
        return petriObjectName;
    }

    public int getPriority() {
        return priority;
    }
}
