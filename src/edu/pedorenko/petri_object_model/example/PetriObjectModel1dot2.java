package edu.pedorenko.petri_object_model.example;

import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModel;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ConstantDelayGenerator;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ExponentialDelayGenerator;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.NormalDelayGenerator;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.UniformDelayGenerator;
import edu.pedorenko.petri_object_model.model_computer.PetriObjectModelComputer;
import edu.pedorenko.petri_object_model.statistics.PetriObjectModelStatistics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PetriObjectModel1dot2 {

    public static void main(String[] args) throws PetriObjectModelException, ExecutionException, InterruptedException {

        PetriObject petriObject1 = createPetriObject1();
        PetriObject petriObject2 = createPetriObject2();

        petriObject1.getTransitionById(2).addArcTo(petriObject2.getPlaceById(3));
        petriObject1.getTransitionById(3).addArcTo(petriObject2.getPlaceById(3));

        List<PetriObject> petriObjects = new ArrayList<>();
        petriObjects.add(petriObject1);
        petriObjects.add(petriObject2);

        PetriObjectModel petriObjectModel = new PetriObjectModel(petriObjects);

        PetriObjectModelStatistics statisticsSequential = PetriObjectModelComputer.computeSequential(petriObjectModel, 100000, new DummyEventProtocol());
        System.out.println("Sequential statistics");
        System.out.println("Transition T2" + statisticsSequential.getPetriObjectStatisticsByPetriObjectId(1).getTransitionStatisticsByTransitionId(2));
        System.out.println("Transition T3" + statisticsSequential.getPetriObjectStatisticsByPetriObjectId(1).getTransitionStatisticsByTransitionId(3));
        System.out.println("Transition T4" + statisticsSequential.getPetriObjectStatisticsByPetriObjectId(2).getTransitionStatisticsByTransitionId(4));
        System.out.println("Transition T5" + statisticsSequential.getPetriObjectStatisticsByPetriObjectId(2).getTransitionStatisticsByTransitionId(5));
        System.out.println("Place P4" + statisticsSequential.getPetriObjectStatisticsByPetriObjectId(2).getPlaceStatisticsByPlaceId(4));
        System.out.println();

        PetriObjectModelStatistics statisticsParallel = PetriObjectModelComputer.computeParallel(petriObjectModel, 100000, new DummyEventProtocolFactory());
        System.out.println("Parallel statistics");
        System.out.println("Transition T2" + statisticsParallel.getPetriObjectStatisticsByPetriObjectId(1).getTransitionStatisticsByTransitionId(2));
        System.out.println("Transition T3" + statisticsParallel.getPetriObjectStatisticsByPetriObjectId(1).getTransitionStatisticsByTransitionId(3));
        System.out.println("Transition T4" + statisticsParallel.getPetriObjectStatisticsByPetriObjectId(2).getTransitionStatisticsByTransitionId(4));
        System.out.println("Transition T5" + statisticsParallel.getPetriObjectStatisticsByPetriObjectId(2).getTransitionStatisticsByTransitionId(5));
        System.out.println("Place P4" + statisticsParallel.getPetriObjectStatisticsByPetriObjectId(2).getPlaceStatisticsByPlaceId(4));
    }

    public static PetriObject createPetriObject1() throws PetriObjectModelException {

        Place p1 = new Place(1, "P1", 1, false);
        Place p2 = new Place(2, "P2", false);

        Transition t1 = new Transition(1, "T1", new ExponentialDelayGenerator(1));
        Transition t2 = new Transition(2, "T2", new ConstantDelayGenerator(5));
        Transition t3 = new Transition(3, "T3", 9, new UniformDelayGenerator(1, 3));

        t1.addArcFrom(p1);
        t1.addArcTo(p1);
        t1.addArcTo(p2);

        t2.addArcFrom(p2);

        t3.addArcFrom(p2);

        List<Transition> transitions = new ArrayList<>();
        transitions.add(t1);
        transitions.add(t2);
        transitions.add(t3);

        return new PetriObject(1, "Object 1", transitions);
    }

    public static PetriObject createPetriObject2() throws PetriObjectModelException {

        Place p3 = new Place(3, "P3", false);
        Place p4 = new Place(4, "P4", true);

        Transition t4 = new Transition(4, "T4", 0.75, new NormalDelayGenerator(4, 1), true);
        Transition t5 = new Transition(5, "T5", 0.25, new ConstantDelayGenerator(4), true);

        t4.addArcFrom(p3);
        t4.addArcTo(p4);

        t5.addArcFrom(p3);
        t5.addArcTo(p4);

        List<Transition> transitions = new ArrayList<>();
        transitions.add(t4);
        transitions.add(t5);

        return new PetriObject(2, "Object 2", transitions);
    }
}
