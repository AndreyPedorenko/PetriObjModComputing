package edu.pedorenko.petri_object_model.model_computer.petri_object_merger;

import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;

abstract class CreateArcTask {

    protected Transition transition;

    protected Place place;

    protected int multiplicity;

    CreateArcTask(Transition transition, Place place, int multiplicity) {
        this.transition = transition;
        this.place = place;
        this.multiplicity = multiplicity;
    }

    Transition getTransition() {
        return transition;
    }

    Place getPlace() {
        return place;
    }

    int getMultiplicity() {
        return multiplicity;
    }

    abstract void doTask();
}
