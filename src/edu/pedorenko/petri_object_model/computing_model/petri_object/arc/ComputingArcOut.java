package edu.pedorenko.petri_object_model.computing_model.petri_object.arc;

import edu.pedorenko.petri_object_model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petri_object_model.computing_model.petri_object.transition.ComputingTransition;

public class ComputingArcOut extends ComputingArc {

    public ComputingArcOut(ComputingTransition transition, ComputingPlace place, int multiplicity) {

        super(transition.getTransitionName() + "|" + place.getPlaceName(),
                place,
                transition,
                multiplicity);
    }

    public ComputingArcOut(ComputingTransition transition, ComputingPlace place) {
        this(transition, place, 1);
    }

    public void runArc(double currentTime) {
        place.increaseMarking(multiplicity);
    }
}
