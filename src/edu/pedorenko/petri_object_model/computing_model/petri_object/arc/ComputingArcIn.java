package edu.pedorenko.petri_object_model.computing_model.petri_object.arc;

import edu.pedorenko.petri_object_model.computing_model.petri_object.place.MarkingException;
import edu.pedorenko.petri_object_model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petri_object_model.computing_model.petri_object.transition.ComputingTransition;

public class ComputingArcIn extends ComputingArc {

    public ComputingArcIn(ComputingPlace place, ComputingTransition transition, int multiplicity) {

        super(place.getPlaceName() + "|" + transition.getTransitionName(),
                place,
                transition,
                multiplicity);
    }

    public ComputingArcIn(ComputingPlace place, ComputingTransition transition) {
        this(place, transition, 1);
    }

    public boolean isRunnable() {
        return place.getMarking() >= multiplicity;
    }

    public void runArc(double currentTime) throws MarkingException {
        place.decreaseMarking(multiplicity);
    }
}
