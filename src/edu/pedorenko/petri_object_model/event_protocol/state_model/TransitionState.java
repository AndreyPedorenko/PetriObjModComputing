package edu.pedorenko.petri_object_model.event_protocol.state_model;

public class TransitionState {

    private TransitionCharacteristics transitionCharacteristics;

    private TransitionBufferState transitionBufferState;

    public TransitionState(TransitionCharacteristics transitionCharacteristics, TransitionBufferState transitionBufferState) {
        this.transitionCharacteristics = transitionCharacteristics;
        this.transitionBufferState = transitionBufferState;
    }

    public TransitionCharacteristics getTransitionCharacteristics() {
        return transitionCharacteristics;
    }

    public TransitionBufferState getTransitionBufferState() {
        return transitionBufferState;
    }
}
