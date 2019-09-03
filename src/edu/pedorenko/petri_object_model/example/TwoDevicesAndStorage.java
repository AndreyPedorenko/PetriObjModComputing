package edu.pedorenko.petri_object_model.example;

import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModel;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ConstantDelayGenerator;
import edu.pedorenko.petri_object_model.model_computer.PetriObjectModelComputer;
import edu.pedorenko.petri_object_model.statistics.PetriObjectModelStatistics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TwoDevicesAndStorage {

    public static void main(String[] args) throws PetriObjectModelException, ExecutionException, InterruptedException {

        List<PetriObject> petriObjects = new ArrayList<>();

        PetriObject taskGenerator = createTaskGenerator();
        PetriObject device1 = createDevice1();
        PetriObject device2 = createDevice2();
        PetriObject storage = createStorage();

        taskGenerator.getTransitionById(1).addArcTo(device1.getPlaceById(2));
        device1.getTransitionById(3).addArcTo(device2.getPlaceById(4));
        device2.getTransitionById(5).addArcTo(storage.getPlaceById(6));
        storage.getTransitionById(6).addArcTo(taskGenerator.getPlaceById(98));

        petriObjects.add(taskGenerator);
        petriObjects.add(device1);
        petriObjects.add(device2);
        petriObjects.add(storage);

        PetriObjectModel petriObjectModel = new PetriObjectModel(petriObjects);
        double timeModeling = 1000000;
        PetriObjectModelStatistics petriObjectModelStatistics = PetriObjectModelComputer.computeParallel(petriObjectModel, timeModeling, new DummyEventProtocolFactory());

        System.out.println(petriObjectModelStatistics.getPetriObjectStatisticsByPetriObjectId(1).getPlaceStatisticsByPlaceId(99).getMarking());
    }

    private static PetriObject createTaskGenerator() throws PetriObjectModelException {

        List<Transition> transitions = new ArrayList<>();

        Place p1 = new Place(1,"P1", 1);

        Transition t1 = new Transition(1, "T1", new ConstantDelayGenerator(40));
        t1.addArcFrom(p1);
        t1.addArcTo(p1);
        transitions.add(t1);

        Place p98 = new Place(98, "P98", 0);
        Place p99 = new Place(99, "P99", 0);

        Transition t2000 = new Transition(2000, "T2000");
        t2000.addArcFrom(p98);
        t2000.addArcTo(p99);
        transitions.add(t2000);

        return new PetriObject(1, "Task Generator", transitions);
    }

    private static PetriObject createDevice1() throws PetriObjectModelException {

        List<Transition> transitions = new ArrayList<>();

        Place p2 = new Place(2, "P2");
        Place p8 = new Place(8, "P8", 1);
        Place p9 = new Place(9, "P9");
        Place p3 = new Place(3,"P3");
        Place p10 = new Place(10, "P10", 3);

        Transition t2 = new Transition(2, "T2", new ConstantDelayGenerator(22));
        t2.addArcFrom(p2);
        t2.addArcFrom(p8);
        t2.addArcTo(p9);
        t2.addArcTo(p3);
        transitions.add(t2);

        Transition t3 = new Transition(3, "T3", new ConstantDelayGenerator(60));
        t3.addArcFrom(p3);
        t3.addArcFrom(p10);
        t3.addArcTo(p10);
        transitions.add(t3);

        Transition t7 = new Transition(7, "T7", new ConstantDelayGenerator(6));
        t7.addArcFrom(p9);
        t7.addArcTo(p8);
        transitions.add(t7);

        return new PetriObject(2, "Device 1", transitions);
    }

    private static PetriObject createDevice2() throws PetriObjectModelException {

        List<Transition> transitions = new ArrayList<>();

        Place p4 = new Place(4, "P4");
        Place p11 = new Place(11,  "P11", 1);
        Place p12 = new Place(12, "P12");
        Place p5 = new Place(5, "P5");
        Place p13 = new Place(13, "P13", 3);

        Transition t4 = new Transition(4, "T4", new ConstantDelayGenerator(23));
        t4.addArcFrom(p4);
        t4.addArcFrom(p11);
        t4.addArcTo(p12);
        t4.addArcTo(p5);
        transitions.add(t4);

        Transition t5 = new Transition(5, "T5", new ConstantDelayGenerator(100));
        t5.addArcFrom(p5);
        t5.addArcFrom(p13);
        t5.addArcTo(p13);
        transitions.add(t5);

        Transition t8 = new Transition(8, "T8", new ConstantDelayGenerator(7));
        t8.addArcFrom(p12);
        t8.addArcTo(p11);
        transitions.add(t8);

        return new PetriObject(3, "Device 2", transitions);
    }

    private static PetriObject createStorage() throws PetriObjectModelException {

        List<Transition> transitions = new ArrayList<>();

        Place p6 = new Place(6, "P6");
        Place p14 = new Place(14, "P14", 1);
        Place p15 = new Place(15, "P15");
        Place p7 = new Place(7, "P7");

        Transition t6 = new Transition(6, "T6", new ConstantDelayGenerator(21));
        t6.addArcFrom(p6);
        t6.addArcFrom(p14);
        t6.addArcTo(p15);
        t6.addArcTo(p7);
        transitions.add(t6);

        Transition t9 = new Transition(9, "T9", new ConstantDelayGenerator(5));
        t9.addArcFrom(p15);
        t9.addArcTo(p14);
        transitions.add(t9);

        return new PetriObject(4, "Storage", transitions);
    }
}
