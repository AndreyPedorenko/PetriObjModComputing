package edu.pedorenko.petri_object_model.model.arc;

import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;

public abstract class Arc {

    private String arcId;

    private Place place;
    private Transition transition;

    private int multiplicity;

    protected Arc(String arcId, Place place, Transition transition, int multiplicity) {

        if (multiplicity <= 0) {
            throw new IllegalArgumentException("Illegal argument multiplicity = " + multiplicity + "\n" +
                    "Arc multiplicity should be more then 0.");
        }

        this.arcId = arcId;
        this.place = place;
        this.transition = transition;
        this.multiplicity = multiplicity;
    }

    public String getArcId() {
        return arcId;
    }

    public Place getPlace() {
        return place;
    }

    public Transition getTransition() {
        return transition;
    }

    public int getMultiplicity() {
        return multiplicity;
    }

    public String toString() {
        return "Arc{" +
                "arcId='" + arcId + '\'' +
                ", place=" + place +
                ", transition=" + transition +
                ", multiplicity=" + multiplicity +
                '}';
    }
}
