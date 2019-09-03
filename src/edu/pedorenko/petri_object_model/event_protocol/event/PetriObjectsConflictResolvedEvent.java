package edu.pedorenko.petri_object_model.event_protocol.event;

import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectCharacteristics;
import java.util.List;

public class PetriObjectsConflictResolvedEvent implements Event {

    private List<PetriObjectCharacteristics> conflictingPetriObjects;

    private PetriObjectCharacteristics selectedPetriObject;

    private double currentTime;

    public PetriObjectsConflictResolvedEvent(
            List<PetriObjectCharacteristics> conflictingPetriObjects,
            PetriObjectCharacteristics selectedPetriObject,
            double currentTime) {

        this.conflictingPetriObjects = conflictingPetriObjects;
        this.selectedPetriObject = selectedPetriObject;
        this.currentTime = currentTime;
    }

    public List<PetriObjectCharacteristics> getConflictingPetriObjects() {
        return conflictingPetriObjects;
    }

    public PetriObjectCharacteristics getSelectedPetriObject() {
        return selectedPetriObject;
    }

    public double getCurrentTime() {
        return currentTime;
    }
}
