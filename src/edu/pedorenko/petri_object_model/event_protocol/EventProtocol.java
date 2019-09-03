package edu.pedorenko.petri_object_model.event_protocol;

import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectActInCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectActOutCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectModelActInCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectModelActOutCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.PetriObjectsConflictResolvedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.TimeMovedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.TransitionActInCompletedEvent;
import edu.pedorenko.petri_object_model.event_protocol.event.TransitionsConflictResolvedEvent;

public interface EventProtocol {

    boolean isEnabled();

    void onTimeMovedEvent(TimeMovedEvent timeMovedEvent);

    void onTransitionActInCompletedEvent(TransitionActInCompletedEvent transitionActInCompletedEvent);

    void onPetriObjectActOutCompletedEvent(PetriObjectActOutCompletedEvent petriObjectActOutCompletedEvent);

    void onPetriObjectActInCompletedEvent(PetriObjectActInCompletedEvent petriObjectActInCompletedEvent);

    void onPetriObjectModelActOutCompletedEvent(PetriObjectModelActOutCompletedEvent petriObjectModelActOutCompletedEvent);

    void onPetriObjectModelActInCompletedEvent(PetriObjectModelActInCompletedEvent petriObjectModelActInCompletedEvent);

    void onPetriObjectsConflictResolvedEvent(PetriObjectsConflictResolvedEvent petriObjectsConflictResolvedEvent);

    void onTransitionsConflictResolvedEvent(TransitionsConflictResolvedEvent transitionsConflictResolvedEvent);
}
