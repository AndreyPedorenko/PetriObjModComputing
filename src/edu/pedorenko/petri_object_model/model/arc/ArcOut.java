package edu.pedorenko.petri_object_model.model.arc;

import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;

public class ArcOut extends Arc {

    public ArcOut(Transition transition, Place place, int multiplicity) {
        super(transition.getTransitionName() + "|" + place.getPlaceName(), place, transition, multiplicity);
    }
}
