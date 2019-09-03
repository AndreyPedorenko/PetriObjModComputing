package edu.pedorenko.petri_object_model.computing_model.petri_object.arc;

import edu.pedorenko.petri_object_model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petri_object_model.computing_model.petri_object.place.MarkingException;
import edu.pedorenko.petri_object_model.computing_model.petri_object.transition.ComputingTransition;

public class ComputingInformationalArcIn extends ComputingArcIn {

    public ComputingInformationalArcIn(ComputingPlace place, ComputingTransition transition, int multiplicity) {
        super(place, transition, multiplicity);
    }

    public ComputingInformationalArcIn(ComputingPlace place, ComputingTransition transition) {
        super(place, transition);
    }

    public void runArc(double currentTime) throws MarkingException {

        //this method doesn't decrease place marking as it is a specific of informational arcs

        int marking = place.getMarking();

        if (marking < multiplicity) {
            throw new MarkingException("Can't run informational arc: marking = " + marking + " is less then " +
                    "arc multiplicity = " + multiplicity);
        }
    }
}
