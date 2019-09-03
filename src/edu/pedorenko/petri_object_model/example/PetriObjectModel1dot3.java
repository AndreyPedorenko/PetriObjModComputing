package edu.pedorenko.petri_object_model.example;

import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModel;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ConstantDelayGenerator;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ExponentialDelayGenerator;
import edu.pedorenko.petri_object_model.model_computer.PetriObjectModelComputer;
import edu.pedorenko.petri_object_model.statistics.PetriObjectModelStatistics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PetriObjectModel1dot3 {

    public static void main(String[] args) throws PetriObjectModelException, ExecutionException, InterruptedException {

        Place p1 = new Place(1, "P1", 1, false);
        Place p2 = new Place(2, "P2", false);
        Place p3 = new Place(3, "P3", 1, true);
        Place p4 = new Place(4, "P4", false);
        Place p5 = new Place(5, "P5", 1, false);
        Place p6 = new Place(6, "P6", true);

        Transition t1 = new Transition(1, "T1", new ExponentialDelayGenerator(1));
        Transition t2 = new Transition(2, "T2", new ExponentialDelayGenerator(3));
        Transition t3 = new Transition(3, "T3", new ConstantDelayGenerator(5));
        Transition t4 = new Transition(4, "T4", new ConstantDelayGenerator(5));

        t1.addArcFrom(p1);
        t1.addArcTo(p1);
        t1.addArcTo(p2);

        t2.addArcFrom(p2);
        t2.addInformationalArcFrom(p3);
        t2.addArcTo(p6);

        t3.addArcFrom(p3);
        t3.addArcFrom(p4);
        t3.addArcTo(p3);
        t3.addArcTo(p5);

        t4.addArcFrom(p5);
        t4.addArcTo(p4);

        List<Transition> transitions = new ArrayList<>();
        transitions.add(t1);
        transitions.add(t2);
        transitions.add(t3);
        transitions.add(t4);

        PetriObject petriObject = new PetriObject(1, "Object1", transitions);
        PetriObjectModel petriObjectModel = new PetriObjectModel(new ArrayList<PetriObject>(){{add(petriObject);}});

        PetriObjectModelStatistics statistics = PetriObjectModelComputer.computeParallel(petriObjectModel, 100000, new DummyEventProtocolFactory());
        System.out.println(statistics.getPetriObjectStatisticsByPetriObjectId(1).getPlaceStatisticsByPlaceId(3));
        System.out.println(statistics.getPetriObjectStatisticsByPetriObjectId(1).getPlaceStatisticsByPlaceId(6));
    }
}
