package edu.pedorenko.petri_object_model.event_protocol.event;

import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionBufferState;
import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionCharacteristics;

public class TransitionActInCompletedEvent implements Event {

    private TransitionCharacteristics transitionCharacteristics;

    private TransitionBufferState transitionBufferStateBeforeActIn;
    private TransitionBufferState transitionBufferStateAfterActIn;

    private double eventTime;

    private double currentTime;

    public TransitionActInCompletedEvent(
            TransitionCharacteristics transitionCharacteristics,
            TransitionBufferState transitionBufferStateBeforeActIn,
            TransitionBufferState transitionBufferStateAfterActIn,
            double eventTime,
            double currentTime) {

        this.transitionCharacteristics = transitionCharacteristics;
        this.transitionBufferStateBeforeActIn = transitionBufferStateBeforeActIn;
        this.transitionBufferStateAfterActIn = transitionBufferStateAfterActIn;
        this.eventTime = eventTime;
        this.currentTime = currentTime;
    }

    public TransitionCharacteristics getTransitionCharacteristics() {
        return transitionCharacteristics;
    }

    public TransitionBufferState getTransitionBufferStateBeforeActIn() {
        return transitionBufferStateBeforeActIn;
    }

    public TransitionBufferState getTransitionBufferStateAfterActIn() {
        return transitionBufferStateAfterActIn;
    }

    public double getEventTime() {
        return eventTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }
}
