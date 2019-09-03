package edu.pedorenko.petri_object_model.computing_model.petri_object.transition;

import edu.pedorenko.petri_object_model.computing_model.petri_object.arc.ComputingArcIn;
import edu.pedorenko.petri_object_model.computing_model.petri_object.arc.ComputingArcOut;
import edu.pedorenko.petri_object_model.computing_model.petri_object.place.MarkingException;
import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionBufferState;
import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionCharacteristics;
import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionState;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.DelayGenerator;
import java.util.ArrayList;
import java.util.List;

public class ComputingTransition {

    private long transitionId;

    private String transitionName;

    private int priority;
    private double probability;

    private DelayGenerator delayGenerator;
    private EventTimeBuffer eventTimeBuffer = new EventTimeBuffer();

    private List<ComputingArcIn> arcsIn = new ArrayList<>();
    private List<ComputingArcOut> arcsOut = new ArrayList<>();

    public ComputingTransition(
            long transitionId,
            String transitionName,
            int priority,
            double probability,
            DelayGenerator delayGenerator) {

        this.transitionId = transitionId;
        this.transitionName = transitionName;
        this.priority = priority;
        this.probability = probability;
        this.delayGenerator = delayGenerator;
    }

    public void addArcIn(ComputingArcIn arcIn) {
        arcsIn.add(arcIn);
    }

    public void addAllArcsIn(List<ComputingArcIn> arcsIn) {
        this.arcsIn.addAll(arcsIn);
    }

    public void addArcOut(ComputingArcOut arcOut) {
        arcsOut.add(arcOut);
    }

    public void addAllArcsOut(List<ComputingArcOut> arcsOut) {
        this.arcsOut.addAll(arcsOut);
    }

    public boolean isActive() {
        return arcsIn.stream().allMatch(ComputingArcIn::isRunnable);
    }

    public double actIn(double currentTime) throws MarkingException {

        for (ComputingArcIn arcIn : arcsIn) {
            arcIn.runArc(currentTime);
        }

        double delay = delayGenerator.get();

        double eventTime = currentTime + delay;

        eventTimeBuffer.addEvent(eventTime);

        return eventTime;
    }

    public void actOut(double currentTime) {
        arcsOut.forEach(arcOut -> arcOut.runArc(currentTime));
        eventTimeBuffer.removeNearestEvent();
    }

    public double getNearestEventTime() {
        return eventTimeBuffer.getNearestEventTime();
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

    public int getEventsAmount() {
        return eventTimeBuffer.getEventsAmount();
    }

    public TransitionState toTransitionState() {
        return new TransitionState(
                getTransitionCharacteristics(),
                getTransitionBufferState());
    }

    public TransitionCharacteristics getTransitionCharacteristics() {
        return new TransitionCharacteristics(
                transitionId,
                transitionName,
                priority,
                probability);
    }

    public TransitionBufferState getTransitionBufferState() {
        return eventTimeBuffer.toTransitionBuffetState();
    }
}
