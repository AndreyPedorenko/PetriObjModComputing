package edu.pedorenko.petri_object_model.example;

import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModel;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ExponentialDelayGenerator;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.NormalDelayGenerator;
import edu.pedorenko.petri_object_model.model_computer.PetriObjectModelComputer;
import edu.pedorenko.petri_object_model.statistics.PetriObjectModelStatistics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PetriObjectModel1dot6 {

    public static void main(String[] args) throws PetriObjectModelException, ExecutionException, InterruptedException {

        Place p1 = new Place(1, "P1", 1, false);
        Transition t1 = new Transition(1, "T1", new ExponentialDelayGenerator(3));
        t1.addArcFrom(p1);
        PetriObject petriObject1 = new PetriObject(1, "Object 1", new ArrayList<Transition>(){{add(t1);}});

        Place p2 = new Place(2, "P2", false);
        Transition t2 = new Transition(2, "T2", new NormalDelayGenerator(3, 1));
        t2.addArcFrom(p2);
        PetriObject petriObject2 = new PetriObject(2, "Object 2", new ArrayList<Transition>(){{add(t2);}});

        petriObject1.getTransitionById(1).addArcTo(petriObject2.getPlaceById(2));
        petriObject2.getTransitionById(2).addArcTo(petriObject1.getPlaceById(1));

        List<PetriObject> petriObjects = new ArrayList<>();
        petriObjects.add(petriObject1);
        petriObjects.add(petriObject2);

        PetriObjectModel petriObjectModel = new PetriObjectModel(petriObjects);

        PetriObjectModelStatistics statisticsSequential = PetriObjectModelComputer.computeSequential(petriObjectModel, 100000, new DummyEventProtocol());
        System.out.println("Sequential statistics");
        System.out.println("Transition T1 " + statisticsSequential.getPetriObjectStatisticsByPetriObjectId(1).getTransitionStatisticsByTransitionId(1));
        System.out.println("Transition T2 " + statisticsSequential.getPetriObjectStatisticsByPetriObjectId(2).getTransitionStatisticsByTransitionId(2));
        System.out.println();

        PetriObjectModelStatistics statisticsParallel = PetriObjectModelComputer.computeParallel(petriObjectModel, 100000, new DummyEventProtocolFactory());
        System.out.println("Parallel statistics");
        System.out.println("Transition T1 " + statisticsParallel.getPetriObjectStatisticsByPetriObjectId(1).getTransitionStatisticsByTransitionId(1));
        System.out.println("Transition T2 " + statisticsParallel.getPetriObjectStatisticsByPetriObjectId(2).getTransitionStatisticsByTransitionId(2));
    }
}
