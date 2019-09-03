package edu.pedorenko.petri_object_model.event_protocol.event;

public class TimeMovedEvent implements Event {

    private double previousTime;

    private double newTime;

    public TimeMovedEvent(double previousTime, double newTime) {
        this.previousTime = previousTime;
        this.newTime = newTime;
    }

    public double getPreviousTime() {
        return previousTime;
    }

    public double getNewTime() {
        return newTime;
    }

    public String toString() {
        return "TimeMovedEvent{" +
                "previousTime=" + previousTime +
                ", newTime=" + newTime +
                '}';
    }
}
