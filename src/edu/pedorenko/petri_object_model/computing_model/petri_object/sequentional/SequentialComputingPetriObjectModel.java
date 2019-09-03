package edu.pedorenko.petri_object_model.computing_model.petri_object.sequentional;

import edu.pedorenko.petri_object_model.computing_model.time.TimeState;
import edu.pedorenko.petri_object_model.event_protocol.EventProtocol;
import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectModelActInCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectModelActOutCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectsConflictResolvedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.TimeMovedEvent;
import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectCharacteristics;
import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectModelState;
import edu.pedorenko.petri_object_model.event_protocol.state_model.PetriObjectState;
import edu.pedorenko.petri_object_model.statistics.PetriObjectModelStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class SequentialComputingPetriObjectModel {

    private EventProtocol eventProtocol;
    private PetriObjectModelStatistics petriObjectModelStatistics;

    private List<SequentialComputingPetriObject> petriObjects;
    private TimeState timeState;

    public SequentialComputingPetriObjectModel(
            List<SequentialComputingPetriObject> petriObjects,
            EventProtocol eventProtocol,
            PetriObjectModelStatistics petriObjectModelStatistics) {

        this.petriObjects = petriObjects;
        this.eventProtocol = eventProtocol;
        this.petriObjectModelStatistics = petriObjectModelStatistics;
    }

    public PetriObjectModelStatistics go(double simulationTime) {

        timeState = new TimeState(simulationTime);

        actIn(timeState.getCurrentTime());

        while (timeState.getCurrentTime() <= timeState.getSimulationTime()) {

            doStatisticsAndMoveTime();

            if (timeState.getCurrentTime() <= timeState.getSimulationTime()) {

                actOut(timeState.getCurrentTime());
                actIn(timeState.getCurrentTime());
            }
        }

        return petriObjectModelStatistics;
    }

    private void doStatisticsAndMoveTime() {

        double previousTime = timeState.getCurrentTime();

        double nearestEventTime = petriObjects.stream()
                .mapToDouble(SequentialComputingPetriObject::getNearestEventTime)
                .min()
                .orElse(timeState.getSimulationTime());

        if (nearestEventTime < timeState.getSimulationTime()) {
            petriObjectModelStatistics.doStatistics((nearestEventTime - previousTime) / nearestEventTime);
        } else {
            petriObjectModelStatistics.doStatistics((timeState.getSimulationTime() - previousTime) / timeState.getSimulationTime());
        }

        timeState.setCurrentTime(nearestEventTime);

        if (eventProtocol.isEnabled()) {
            TimeMovedEvent timeMovedEvent = new TimeMovedEvent(previousTime, nearestEventTime);
            eventProtocol.onTimeMovedEvent(timeMovedEvent);
        }
    }

    private void actIn(double currentTime) {

        List<SequentialComputingPetriObject> activePetriObject = getActivePetriObjects();

        while (!activePetriObject.isEmpty()) {

            SequentialComputingPetriObject chosenPetriObject = resolveConflict(activePetriObject);
            chosenPetriObject.actIn(currentTime);

            activePetriObject = getActivePetriObjects();
        }

        if (eventProtocol.isEnabled()) {
            PetriObjectModelState petriObjectModelState = toPetriObjectModelState();
            eventProtocol.onPetriObjectModelActInCompletedEvent(
                    new PetriObjectModelActInCompletedEvent(petriObjectModelState, currentTime));
        }
    }

    private void actOut(double currentTime) {

        petriObjects.forEach(petriObject -> petriObject.actOut(currentTime));

        if (eventProtocol.isEnabled()) {
            PetriObjectModelState petriObjectModelState = toPetriObjectModelState();
            eventProtocol.onPetriObjectModelActOutCompletedEvent(
                    new PetriObjectModelActOutCompletedEvent(petriObjectModelState, currentTime));
        }
    }

    private SequentialComputingPetriObject resolveConflict(List<SequentialComputingPetriObject> activePetriObjects) {

        List<PetriObjectCharacteristics> conflictingPetriObjects = null;
        if (eventProtocol.isEnabled()) {
            conflictingPetriObjects = activePetriObjects.stream()
                    .map(SequentialComputingPetriObject::getPetriObjectCharacteristics)
                    .collect(Collectors.toList());
        }

        List<SequentialComputingPetriObject> activePrioritisedPetriObjects = selectPrioritisedPetriObjects(activePetriObjects);

        SequentialComputingPetriObject selectedPetriObject = selectRandomPetriObject(activePrioritisedPetriObjects);

        if (eventProtocol.isEnabled()) {
            eventProtocol.onPetriObjectsConflictResolvedEvent(
                    new PetriObjectsConflictResolvedEvent(
                            conflictingPetriObjects,
                            selectedPetriObject.getPetriObjectCharacteristics(),
                            timeState.getCurrentTime()));
        }

        return selectedPetriObject;
    }

    private List<SequentialComputingPetriObject> selectPrioritisedPetriObjects(List<SequentialComputingPetriObject> activePetriObjects) {

        int maxPriority = activePetriObjects.stream()
                .mapToInt(SequentialComputingPetriObject::getPriority)
                .max()
                .getAsInt();

        return activePetriObjects.stream()
                .filter(petriObject -> petriObject.getPriority() == maxPriority)
                .collect(Collectors.toList());
    }

    private SequentialComputingPetriObject selectRandomPetriObject(List<SequentialComputingPetriObject> petriObjects) {

        double random = Math.random();
        double sum = 0;

        for (SequentialComputingPetriObject petriObject : petriObjects) {

            sum += 1d / petriObjects.size();

            if (random < sum) {
                return petriObject;
            }
        }

        return petriObjects.get(petriObjects.size() - 1);
    }

    private List<SequentialComputingPetriObject> getActivePetriObjects() {

        return petriObjects.stream()
                .filter(SequentialComputingPetriObject::isActive)
                .collect(Collectors.toList());
    }

    private PetriObjectModelState toPetriObjectModelState() {
        List<PetriObjectState> petriObjectsState = petriObjects.stream()
                .map(SequentialComputingPetriObject::toPetriObjectState)
                .collect(Collectors.toList());
        return new PetriObjectModelState(petriObjectsState);
    }
}
