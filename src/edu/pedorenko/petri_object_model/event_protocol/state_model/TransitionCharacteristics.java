package edu.pedorenko.petri_object_model.event_protocol.state_model;

public class TransitionCharacteristics {

    private long transitionId;

    private String transitionName;

    private int priority;
    private double probability;

    public TransitionCharacteristics(long transitionId, String transitionName, int priority, double probability) {
        this.transitionId = transitionId;
        this.transitionName = transitionName;
        this.priority = priority;
        this.probability = probability;
    }

    public long getTransitionId() {
        return transitionId;
    }

    public String getTransitionName() {
        return transitionName;
    }

    public int getPriority() {
        return priority;
    }

    public double getProbability() {
        return probability;
    }
}
