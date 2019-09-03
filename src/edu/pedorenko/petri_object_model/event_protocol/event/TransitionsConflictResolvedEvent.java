package edu.pedorenko.petri_object_model.event_protocol.event;

import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionCharacteristics;
import java.util.List;

public class TransitionsConflictResolvedEvent implements Event {

    private List<TransitionCharacteristics> conflictingTransitions;

    private TransitionCharacteristics selectedTransition;

    private double currentTime;

    public TransitionsConflictResolvedEvent(
            List<TransitionCharacteristics> conflictingTransitions,
            TransitionCharacteristics selectedTransition,
            double currentTime) {

        this.conflictingTransitions = conflictingTransitions;
        this.selectedTransition = selectedTransition;
        this.currentTime = currentTime;
    }

    public List<TransitionCharacteristics> getConflictingTransitions() {
        return conflictingTransitions;
    }

    public TransitionCharacteristics getSelectedTransition() {
        return selectedTransition;
    }

    public double getCurrentTime() {
        return currentTime;
    }
}
