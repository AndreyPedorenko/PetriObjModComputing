package edu.pedorenko.petri_object_model.computing_model.petri_object.arc;

import edu.pedorenko.petri_object_model.computing_model.petri_object.place.MarkingException;
import edu.pedorenko.petri_object_model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petri_object_model.computing_model.petri_object.transition.ComputingTransition;

public abstract class ComputingArc {

    protected String arcId;

    protected ComputingPlace place;
    protected ComputingTransition transition;

    protected int multiplicity;

    public ComputingArc(String arcId, ComputingPlace place, ComputingTransition transition, int multiplicity) {

        if (multiplicity <= 0) {
            throw new IllegalArgumentException("Illegal argument multiplicity = " + multiplicity + "\n" +
                    "Arc multiplicity should be more then 0.");
        }

        this.arcId = arcId;
        this.place = place;
        this.transition = transition;
        this.multiplicity = multiplicity;
    }

    public ComputingArc(String arcId, ComputingPlace place, ComputingTransition transition) {
        this(arcId, place, transition, 1);
    }

    public abstract void runArc(double currentTime) throws MarkingException;

    public String getArcId() {
        return arcId;
    }

    public ComputingPlace getPlace() {
        return place;
    }

    public ComputingTransition getTransition() {
        return transition;
    }

    public int getMultiplicity() {
        return multiplicity;
    }
}
