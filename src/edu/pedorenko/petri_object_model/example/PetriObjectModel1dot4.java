package edu.pedorenko.petri_object_model.example;

import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModel;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ExponentialDelayGenerator;
import java.util.ArrayList;
import java.util.List;

public class PetriObjectModel1dot4 {

    public static void main(String[] args) throws PetriObjectModelException {

        Place p1 = new Place(1, "P1", 1);

        Transition t1 = new Transition(1, "T1", new ExponentialDelayGenerator(1));
        Transition t2 = new Transition(2, "T2", new ExponentialDelayGenerator(3));

        t1.addArcTo(p1);
        t2.addArcFrom(p1);

        List<Transition> transitions = new ArrayList<>();
        transitions.add(t1);
        transitions.add(t2);

        PetriObject petriObject = new PetriObject(1, "Object 1", transitions);

        try {
            PetriObjectModel petriObjectModel = new PetriObjectModel(new ArrayList<PetriObject>() {{
                add(petriObject);
            }});
        } catch (PetriObjectModelException ex) {
            System.out.println("Received exception as expected. Exception message: \"" + ex.getMessage() + "\"");
        }
    }
}
