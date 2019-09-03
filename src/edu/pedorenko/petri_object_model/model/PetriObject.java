package edu.pedorenko.petri_object_model.model;

import edu.pedorenko.petri_object_model.model.arc.ArcIn;
import edu.pedorenko.petri_object_model.model.arc.ArcOut;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PetriObject implements Iterable<Transition> {

    private List<Long> petriObjectIds; //several petri object can be connected to one with an algorithm

    private String petriObjectName;

    private Map<Long, Transition> transitionsMap = new HashMap<>();
    private Map<Long, Place> placesMap = new HashMap<>();

    private int priority;

    private int externalBufferSizeLimit;

    public PetriObject(
            List<Long> petriObjectIds,
            String petriObjectName,
            List<Transition> transitions,
            int priority,
            int externalBufferSizeLimit) throws PetriObjectModelException {

        this.petriObjectIds = petriObjectIds;

        this.petriObjectName = petriObjectName;
        this.priority = priority;

        this.externalBufferSizeLimit = externalBufferSizeLimit;

        fillMaps(transitions);
    }

    public PetriObject(
            long petriObjectId,
            String petriObjectName,
            List<Transition> transitions,
            int priority,
            int externalBufferSizeLimit) throws PetriObjectModelException {

        this(new ArrayList<Long>(){{add(petriObjectId);}}, petriObjectName, transitions, priority, externalBufferSizeLimit);
    }

    public PetriObject(long petriObjectId, String petriObjectName, List<Transition> transitions, int priority) throws PetriObjectModelException {
        this(petriObjectId, petriObjectName, transitions, priority, Integer.MAX_VALUE);
    }

    public PetriObject(long petriObjectId, String petriObjectName, List<Transition> transitions) throws PetriObjectModelException {
        this(petriObjectId, petriObjectName, transitions, 1);
    }

    public PetriObject(long petriObjectId, String petriObjectName, int externalBufferSizeLimit, List<Transition> transitions) throws PetriObjectModelException {
        this(petriObjectId, petriObjectName, transitions, 1, externalBufferSizeLimit);
    }

    private void fillMaps(List<Transition> transitions) throws PetriObjectModelException {

        for (Transition transition : transitions) {

            for (ArcIn arcIn : transition.getArcsIn()) {

                Place place = arcIn.getPlace();
                addPlace(place);
            }

            for (ArcOut arcOut : transition.getArcsOut()) {

                Place place = arcOut.getPlace();
                addPlace(place);
            }

            addTransition(transition);
        }
    }

    private void addPlace(Place place) throws PetriObjectModelException {

        long placeId = place.getPlaceId();

        if (placesMap.containsKey(placeId)) {

            Place placeInMap = placesMap.get(placeId);
            if (placeInMap != place) { //really compare by links
                throw new PetriObjectModelException("Different places with same id in Petri object.\n" +
                        "Place 1: \n" + placeInMap + "\n" +
                        "Place 2: \n" + place);
            }

        } else {
            placesMap.put(placeId, place);
            place.setPetriObject(this);
        }
    }

    private void addTransition(Transition transition) throws PetriObjectModelException {

        long transitionId = transition.getTransitionId();

        if (transitionsMap.containsKey(transitionId)) {

            Transition transitionInMap = transitionsMap.get(transitionId);
            if (transitionInMap != transition) { //really compare by links
                throw new PetriObjectModelException("Different transitions with same id in Petri object.\n" +
                        "Transition 1: \n" + transitionInMap + "\n" +
                        "Transition 2: \n" + transition);
            }
        } else {
            transitionsMap.put(transitionId, transition);
            transition.setPetriObject(this);
        }
    }

    public Transition getTransitionById(long transitionId) {
        return transitionsMap.get(transitionId);
    }

    public Place getPlaceById(long placeId) {
        return placesMap.get(placeId);
    }

    public List<Long> getPetriObjectIds() {
        return petriObjectIds;
    }

    public long getPetriObjectId() {
        return petriObjectIds.get(0);
    }

    public String getPetriObjectName() {
        return petriObjectName;
    }

    public int getPriority() {
        return priority;
    }

    public int getExternalBufferSizeLimit() {
        return externalBufferSizeLimit;
    }

    public Collection<Place> getThisObjectPlaces() {
        return placesMap.values();
    }

    public Iterator<Transition> iterator() {
        return transitionsMap.values().iterator();
    }

    void validate(Set<Long> transitionIdsSet, Set<Long> placeIdsSet) throws PetriObjectModelException {

        for (Transition transition : transitionsMap.values()) {

            long transitionId = transition.getTransitionId();
            if (transitionIdsSet.contains(transitionId)) {
                throw new PetriObjectModelException("Duplicate transitionId=" + transitionId + " found." +
                        " Transition ids should be unique event in different Petri objects");
            }

            transitionIdsSet.add(transitionId);

            transition.validate();
        }

        for (Place place : placesMap.values()) {

            long placeId = place.getPlaceId();
            if (placeIdsSet.contains(placeId)) {
                throw new PetriObjectModelException("Duplicate placeId=" + placeId + " found." +
                        " Place ids should be unique event in different Petri objects");
            }

            placeIdsSet.add(placeId);
        }
    }
}
