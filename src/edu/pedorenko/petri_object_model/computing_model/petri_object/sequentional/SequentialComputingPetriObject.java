package edu.pedorenko.petri_object_model.computing_model.petri_object.sequentional;

import edu.pedorenko.petri_object_model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petri_object_model.computing_model.petri_object.transition.ComputingTransition;
import edu.pedorenko.petri_object_model.event_protocol.EventProtocol;
import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectActInCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectActOutCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.TransitionActInCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.TransitionsConflictResolvedEvent;
import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectCharacteristics;
import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectState;
import edu.pedorenko.petri_object_model.event_protocol.state_model.PlaceState;
import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionBufferState;
import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionCharacteristics;
import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionState;
import java.util.List;
import java.util.stream.Collectors;

public class SequentialComputingPetriObject {

    protected EventProtocol eventProtocol;

    private List<Long> petriObjectIds;

    private String name;

    private List<ComputingTransition> transitions;
    private List<ComputingPlace> places;

    private int priority;

    protected double nearestEventTime;

    public SequentialComputingPetriObject(
            List<Long> petriObjectIds,
            String name,
            List<ComputingTransition> transitions,
            List<ComputingPlace> places,
            int priority,
            EventProtocol eventProtocol) {

        this.petriObjectIds = petriObjectIds;
        this.name = name;
        this.transitions = transitions;
        this.places = places;
        this.priority = priority;
        this.nearestEventTime = Double.MAX_VALUE;
        this.eventProtocol = eventProtocol;
    }

    protected void actIn(double currentTime) {

        List<ComputingTransition> activeTransitions = getActiveTransitions();

        while (!activeTransitions.isEmpty()) {

            ComputingTransition chosenTransition = resolveConflict(activeTransitions, currentTime);

            TransitionBufferState transitionBufferStateBeforeActIn = null;
            if (eventProtocol.isEnabled()) {
                transitionBufferStateBeforeActIn = chosenTransition.getTransitionBufferState();
            }

            double eventTime;
            try {
                eventTime = chosenTransition.actIn(currentTime);
            } catch (Exception ex) {
                throw new RuntimeException("Unexpected exception while acting in transition: " + chosenTransition);
            }

            if (eventProtocol.isEnabled()) {
                TransitionCharacteristics transitionCharacteristics = chosenTransition.getTransitionCharacteristics();
                TransitionBufferState transitionBufferStateAfterActIn = chosenTransition.getTransitionBufferState();
                eventProtocol.onTransitionActInCompletedEvent(
                        new TransitionActInCompletedEvent(
                                transitionCharacteristics,
                                transitionBufferStateBeforeActIn,
                                transitionBufferStateAfterActIn,
                                eventTime,
                                currentTime));
            }


            activeTransitions = getActiveTransitions();
        }

        defineNearestEventTime();

        if (eventProtocol.isEnabled()) {
            PetriObjectState petriObjectState = toPetriObjectState();
            eventProtocol.onPetriObjectActInCompletedEvent(
                    new PetriObjectActInCompletedEvent(petriObjectState, currentTime));
        }
    }

    protected void actOut(double currentTime) {

        if (currentTime == nearestEventTime) {

            for (ComputingTransition transition : transitions) {
                while (transition.getNearestEventTime() == currentTime) {
                    transition.actOut(currentTime);
                }
            }

            defineNearestEventTime();

            if (eventProtocol.isEnabled()) {
                PetriObjectState petriObjectState = toPetriObjectState();
                eventProtocol.onPetriObjectActOutCompletedEvent(
                        new PetriObjectActOutCompletedEvent(petriObjectState, currentTime));
            }
        }
    }

    private List<ComputingTransition> getActiveTransitions() {
        return transitions.stream()
                .filter(ComputingTransition::isActive)
                .collect(Collectors.toList());
    }

    private ComputingTransition resolveConflict(List<ComputingTransition> activeTransitions, double currentTime) {

        List<TransitionCharacteristics> conflictingTransitions = null;
        if (eventProtocol.isEnabled()) {
            conflictingTransitions = activeTransitions.stream()
                    .map(ComputingTransition::getTransitionCharacteristics)
                    .collect(Collectors.toList());
        }

        List<ComputingTransition> activePrioritisedTransactions = selectPrioritisedTransitions(activeTransitions);

        ComputingTransition selecterTransition = selectRandomTransition(activePrioritisedTransactions);

        if (eventProtocol.isEnabled()) {
            eventProtocol.onTransitionsConflictResolvedEvent(
                    new TransitionsConflictResolvedEvent(
                            conflictingTransitions,
                            selecterTransition.getTransitionCharacteristics(),
                            currentTime));
        }

        return selecterTransition;
    }

    private List<ComputingTransition> selectPrioritisedTransitions(List<ComputingTransition> transitions) {

        int maxPriority = transitions.stream()
                .mapToInt(ComputingTransition::getPriority)
                .max()
                .getAsInt();

        return transitions.stream()
                .filter(transition -> transition.getPriority() == maxPriority)
                .collect(Collectors.toList());
    }

    private ComputingTransition selectRandomTransition(List<ComputingTransition> transitions) {

        double probabilitiesSum = transitions.stream().mapToDouble(ComputingTransition::getProbability).sum();
        double random = Math.random();
        double sum = 0;

        for (ComputingTransition transition : transitions) {

            sum += transition.getProbability() / probabilitiesSum;

            if (random < sum) {
                return transition;
            }
        }

        return transitions.get(transitions.size() - 1);
    }

    private void defineNearestEventTime() {
        nearestEventTime = transitions.stream()
                .mapToDouble(ComputingTransition::getNearestEventTime)
                .min()
                .orElse(Double.MAX_VALUE);
    }

    boolean isActive() {
        return transitions.stream().anyMatch(ComputingTransition::isActive);
    }

    public List<Long> getPetriObjectIds() {
        return petriObjectIds;
    }

    public String getName() {
        return name;
    }

    int getPriority() {
        return priority;
    }

    double getNearestEventTime() {
        return nearestEventTime;
    }

    PetriObjectState toPetriObjectState() {

        List<TransitionState> transitionsState = transitions.stream()
                .map(ComputingTransition::toTransitionState)
                .collect(Collectors.toList());

        List<PlaceState> placesState = places.stream()
                .map(ComputingPlace::toPlaceState)
                .collect(Collectors.toList());

        return new PetriObjectState(
                getPetriObjectCharacteristics(),
                transitionsState,
                placesState,
                nearestEventTime);
    }

    PetriObjectCharacteristics getPetriObjectCharacteristics() {
        return new PetriObjectCharacteristics(petriObjectIds, name, priority);
    }
}
