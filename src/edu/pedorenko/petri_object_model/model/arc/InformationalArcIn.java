package edu.pedorenko.petri_object_model.model.arc;

import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;

public class InformationalArcIn extends ArcIn {

    public InformationalArcIn(Place place, Transition transition, int multiplicity) {
        super(place, transition, multiplicity);
    }
}
