package edu.pedorenko.petri_object_model.example;

import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModel;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ConstantDelayGenerator;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ExponentialDelayGenerator;
import java.util.ArrayList;
import java.util.List;

public class PetriObjectModel1dot5 {

    public static void main(String[] args) throws PetriObjectModelException {

        Place p1 = new Place(1, "P1", 1, true);
        Place p2 = new Place(2, "P2", false);
        Place p3 = new Place(3, "P3", 1, false);
        Place p4 = new Place(4, "P4", true);

        Transition t1 = new Transition(1, "T1", new ExponentialDelayGenerator(3));
        Transition t2 = new Transition(2, "T2", new ConstantDelayGenerator(5));
        Transition t3 = new Transition(3, "T3", new ConstantDelayGenerator(5));

        t1.addInformationalArcFrom(p1);
        t1.addArcTo(p4);

        t2.addArcFrom(p1);
        t2.addArcFrom(p2);
        t2.addArcTo(p1);
        t2.addArcTo(p3);

        t3.addArcFrom(p3);
        t3.addArcTo(p2);

        List<Transition> transitions = new ArrayList<>();
        transitions.add(t1);
        transitions.add(t2);
        transitions.add(t3);

        PetriObject petriObject = new PetriObject(1, "Object1", transitions);
        try {
            PetriObjectModel petriObjectModel = new PetriObjectModel(new ArrayList<PetriObject>() {{
                add(petriObject);
            }});
        } catch (PetriObjectModelException ex) {
            System.out.println("Received exception as expected. Exception message: \"" + ex.getMessage() + "\"");
        }
    }
}
