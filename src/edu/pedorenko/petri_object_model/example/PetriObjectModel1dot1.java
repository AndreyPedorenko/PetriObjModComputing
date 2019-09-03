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

public class PetriObjectModel1dot1 {

    public static void main(String[] args) throws PetriObjectModelException, ExecutionException, InterruptedException {


        PetriObject petriObject1 = createPetriObject1();

        PetriObject petriObject2 = createPetriObject2();

        PetriObject petriObject3 = createPetriObject3();

        PetriObject petriObject4 = createPetriObject4();

        List<PetriObject> petriObjectList = new ArrayList<>();
        petriObjectList.add(petriObject1);
        petriObjectList.add(petriObject2);
        petriObjectList.add(petriObject3);
        petriObjectList.add(petriObject4);

        petriObject1.getTransitionById(1).addArcTo(petriObject4.getPlaceById(4), 7);
        petriObject1.getTransitionById(1).addArcTo(petriObject3.getPlaceById(2));
        petriObject1.getTransitionById(1).addArcTo(petriObject2.getPlaceById(3));

        petriObject3.getTransitionById(3).addArcTo(petriObject2.getPlaceById(5));
        petriObject4.getTransitionById(4).addArcTo(petriObject2.getPlaceById(6));

        PetriObjectModel petriObjectModel = new PetriObjectModel(petriObjectList);

        PetriObjectModelStatistics statisticsSequential = PetriObjectModelComputer.computeSequential(petriObjectModel, 100000, new DummyEventProtocol());
        PetriObjectModelStatistics statisticsParallel = PetriObjectModelComputer.computeParallel(petriObjectModel, 100000, new DummyEventProtocolFactory());

        System.out.println("Sequential statistics");
        System.out.println("Place P7: " + statisticsSequential.getPetriObjectStatisticsByPetriObjectId(2).getPlaceStatisticsByPlaceId(7));
        System.out.println();
        System.out.println("Parallel statistics");
        System.out.println("Place P7: " + statisticsParallel.getPetriObjectStatisticsByPetriObjectId(2).getPlaceStatisticsByPlaceId(7));

    }

    private static PetriObject createPetriObject1() throws PetriObjectModelException {

        Place p1 = new Place(1, "P1", 1, false);
        Transition t1 = new Transition(1, "T1", new ConstantDelayGenerator(0.5));
        t1.addArcFrom(p1);
        t1.addArcTo(p1);
        return new PetriObject(1, "Object 1", new ArrayList<Transition>(){{add(t1);}});
    }

    private static PetriObject createPetriObject2() throws PetriObjectModelException {

        Place p3 = new Place(3, "P3", false);
        Place p5 = new Place(5, "P5", false);
        Place p6 = new Place(6, "P6", false);
        Place p7 = new Place(7, "P7", true);
        Transition t2 = new Transition(2, "T2", new NormalDelayGenerator(3, 1));
        t2.addArcFrom(p5);
        t2.addArcFrom(p3);
        t2.addArcFrom(p6);
        t2.addArcTo(p7);
        return new PetriObject(2, "Object 2", new ArrayList<Transition>(){{add(t2);}});

    }

    private static PetriObject createPetriObject3() throws PetriObjectModelException {
        Place p2 = new Place(2, "P2", false);
        Transition t3 = new Transition(3, "T3", new UniformDelayGenerator(5, 9));
        t3.addArcFrom(p2, 3);
        return new PetriObject(3, "Object 3", new ArrayList<Transition>(){{add(t3);}});
    }

    private static PetriObject createPetriObject4() throws PetriObjectModelException {
        Place p4 = new Place(4, "P4", false);
        Transition t4 = new Transition(4, "T4", new ExponentialDelayGenerator(7));
        t4.addArcFrom(p4);
        return new PetriObject(4, "Object 4", new ArrayList<Transition>(){{add(t4);}});
    }
}
