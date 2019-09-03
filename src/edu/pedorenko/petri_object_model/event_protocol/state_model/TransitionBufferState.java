package edu.pedorenko.petri_object_model.event_protocol.state_model;

import java.util.List;

public class TransitionBufferState {

    private List<Double> eventTimesBuffer;
    private int eventsAmount;

    private double nearestEventTime;
    private int nearestEventTimeId;

    public TransitionBufferState(List<Double> eventTimesBuffer, int eventsAmount, double nearestEventTime, int nearestEventTimeId) {
        this.eventTimesBuffer = eventTimesBuffer;
        this.eventsAmount = eventsAmount;
        this.nearestEventTime = nearestEventTime;
        this.nearestEventTimeId = nearestEventTimeId;
    }

    public List<Double> getEventTimesBuffer() {
        return eventTimesBuffer;
    }

    public int getEventsAmount() {
        return eventsAmount;
    }

    public double getNearestEventTime() {
        return nearestEventTime;
    }

    public int getNearestEventTimeId() {
        return nearestEventTimeId;
    }
}
