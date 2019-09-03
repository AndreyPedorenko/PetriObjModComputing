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

public class PetriObjectModel1dot7 {

    public static void main(String[] args) throws PetriObjectModelException, ExecutionException, InterruptedException {

        Place p1 = new Place(1, "P1", 1, false);
        Place p2 = new Place(2, "P2", false);
        Place p4 = new Place(4, "P4", true);

        Transition t1 = new Transition(1, "T1", new ConstantDelayGenerator(5));
        Transition t2 = new Transition(2, "T2", 0.3, new ExponentialDelayGenerator(1));

        t1.addArcFrom(p1);
        t1.addArcTo(p1);
        t1.addArcTo(p2);

        t2.addArcFrom(p2);
        t2.addArcTo(p4);

        List<Transition> transitions1 = new ArrayList<>();
        transitions1.add(t1);
        transitions1.add(t2);

        PetriObject petriObject1 = new PetriObject(1, "Object 1", transitions1);

        Place p3 = new Place(3, "P3", true);
        Transition t3 = new Transition(3, "T3", new ExponentialDelayGenerator(3));
        t3.addArcTo(p3);
        PetriObject petriObject2 = new PetriObject(2, "Object 2", new ArrayList<Transition>(){{add(t3);}});

        petriObject2.getTransitionById(3).addArcFrom(petriObject1.getPlaceById(2));

        List<PetriObject> petriObjects = new ArrayList<>();
        petriObjects.add(petriObject1);
        petriObjects.add(petriObject2);

        PetriObjectModel petriObjectModel = new PetriObjectModel(petriObjects);

        PetriObjectModelStatistics statistics = PetriObjectModelComputer.computeParallel(petriObjectModel, 100000, new DummyEventProtocolFactory());
        System.out.println(statistics.getPetriObjectStatisticsByPetriObjectId(1).getTransitionStatisticsByTransitionId(2));
        System.out.println(statistics.getPetriObjectStatisticsByPetriObjectId(1).getPlaceStatisticsByPlaceId(4));
        System.out.println(statistics.getPetriObjectStatisticsByPetriObjectId(2).getTransitionStatisticsByTransitionId(3));
        System.out.println(statistics.getPetriObjectStatisticsByPetriObjectId(2).getPlaceStatisticsByPlaceId(3));


    }

}
