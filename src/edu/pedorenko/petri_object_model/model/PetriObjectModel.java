package edu.pedorenko.petri_object_model.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PetriObjectModel implements Iterable<PetriObject> {

    private List<PetriObject> petriObjects;

    public PetriObjectModel(List<PetriObject> petriObjects) throws PetriObjectModelException {
        this.petriObjects = petriObjects;
        validate();
    }

    private void validate() throws PetriObjectModelException {
        Set<Long> petriObjectIdsSet = new HashSet<>();
        Set<Long> transitionIdsSet = new HashSet<>();
        Set<Long> placeIdsSet = new HashSet<>();
        for (PetriObject petriObject : petriObjects) {

            for (Long petriObjectId : petriObject.getPetriObjectIds()) {

                if (petriObjectIdsSet.contains(petriObjectId)) {
                    throw new PetriObjectModelException("Duplicate petriObjectId=" + petriObjectId + " found in petri object model");
                }

                petriObjectIdsSet.add(petriObjectId);
            }

            petriObject.validate(transitionIdsSet, placeIdsSet);
        }
    }

    public Iterator<PetriObject> iterator() {
        return petriObjects.iterator();
    }
}
