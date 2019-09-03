package edu.pedorenko.petri_object_model.model_computer.cycles_resolver;

import edu.pedorenko.petri_object_model.model.PetriObject;
import java.util.ArrayList;
import java.util.List;

class PetriObjectGraphVertex {

    private PetriObject petriObject;

    private List<PetriObjectGraphVertex> children = new ArrayList<>();

    private List<PetriObjectGraphVertex> reversedChildren = new ArrayList<>();

    PetriObjectGraphVertex(PetriObject petriObject) {
        this.petriObject = petriObject;
    }

    void addChild(PetriObjectGraphVertex petriObjectGraphVertex) {
        children.add(petriObjectGraphVertex);
    }

    void addReversedChild(PetriObjectGraphVertex petriObjectGraphVertex) {
        reversedChildren.add(petriObjectGraphVertex);
    }

    PetriObject getPetriObject() {
        return petriObject;
    }

    List<PetriObjectGraphVertex> getChildren() {
        return children;
    }

    List<PetriObjectGraphVertex> getReversedChildren() {
        return reversedChildren;
    }
}
