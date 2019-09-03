package edu.pedorenko.petri_object_model.model.arc;

import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;

public class ArcIn extends Arc {

    public ArcIn(Place place, Transition transition, int multiplicity) {

        super(place.getPlaceName() + "|" + transition.getTransitionName(), place, transition, multiplicity);
    }
}
