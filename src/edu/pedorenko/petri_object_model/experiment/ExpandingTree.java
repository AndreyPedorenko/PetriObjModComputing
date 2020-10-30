package edu.pedorenko.petri_object_model.experiment;

import edu.pedorenko.petri_object_model.example.DummyEventProtocol;
import edu.pedorenko.petri_object_model.example.DummyEventProtocolFactory;
import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModel;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.DelayGenerator;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ExponentialDelayGenerator;
import edu.pedorenko.petri_object_model.model_computer.PetriObjectModelComputer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ExpandingTree {

    private static final int petriObjectsAmount = 10;
    private static final int mSSesInOneObjectAmount = 10;
    private static final int channelsInOneMSS = 1;
    private static final DelayGenerator generatorDelayGenerator = new ExponentialDelayGenerator(2);
    private static final DelayGenerator mSSDelayGenerator = new ExponentialDelayGenerator(1);
    private static final double timeModeling = 100000;
    private static final int BRANCHES = 2;

    public static void main(String[] args) throws Exception {

        System.out.println("Branches: " + BRANCHES);

        for (int i = 0; i < 10; ++i) {

            PetriObjectModel model = createExpandingTree(
                petriObjectsAmount * (i + 1),
                BRANCHES,
                mSSesInOneObjectAmount,
                channelsInOneMSS,
                generatorDelayGenerator,
                mSSDelayGenerator);

            long sequentialTimeStart = System.currentTimeMillis();
            PetriObjectModelComputer.computeSequential(model, timeModeling, new DummyEventProtocol());
            long sequentialTimeEnd = System.currentTimeMillis();

            long parallelTimeStart = System.currentTimeMillis();
            PetriObjectModelComputer.computeParallel(model, timeModeling, new DummyEventProtocolFactory());
            long parallelTimeEnd = System.currentTimeMillis();

            System.out.println("Test #" + (i + 1));
            System.out.println("Petri objects: " + petriObjectsAmount * (i + 1));
            System.out.println("Time modeling: " + timeModeling);
            System.out.println("Sequential real time: " + (sequentialTimeEnd - sequentialTimeStart));
            System.out.println("Parallel real time: " + (parallelTimeEnd - parallelTimeStart));
            System.out.println();
            System.out.println();
        }
    }

    private static PetriObjectModel createExpandingTree(
        int objectsAmount,
        int branches,
        int mSSesInOneObjectAmount,
        int channels,
        DelayGenerator generatorDelayGenerator,
        DelayGenerator mSSDelayGenerator) throws PetriObjectModelException {

        List<PetriObject> petriObjects = new ArrayList<>();
        Queue<PetriObject> petriObjectsQueue = new LinkedList<>();

        PetriObject generator = createGenerator(generatorDelayGenerator);
        petriObjects.add(generator);
        petriObjectsQueue.offer(generator);
        for (int i = 1; i < objectsAmount; i += branches) {
            PetriObject petriObjectToAdd = petriObjectsQueue.poll();
            long petriObjectId = petriObjectToAdd.getPetriObjectId();
            for (int branch = 0; branch < branches; ++branch) {
                int mssGroupId = i + branch;
                if (mssGroupId >= objectsAmount) {
                    break;
                }
                PetriObject mSS = createMSSGroup(mssGroupId, mSSesInOneObjectAmount, channels, mSSDelayGenerator);
                petriObjects.add(mSS);
                petriObjectsQueue.offer(mSS);
                petriObjectToAdd.getTransitionById(petriObjectId * mSSesInOneObjectAmount)
                    .addArcTo(mSS.getPlaceById((mssGroupId - 1) * (mSSesInOneObjectAmount * 2) + 1));
            }
        }

        return new PetriObjectModel(petriObjects);
    }

    private static PetriObject createGenerator(DelayGenerator generatorDelayGenerator)
        throws PetriObjectModelException {
        Place generatorPlace = new Place(0, "Generator place", 1, false);
        Transition generator = new Transition(0, "Generator", generatorDelayGenerator, false);

        generator.addArcFrom(generatorPlace);
        generator.addArcTo(generatorPlace);

        return new PetriObject(0, "Generator", new ArrayList<Transition>() {{
            add(generator);
        }});
    }

    private static PetriObject createMSSGroup(
        int mSSGroupId, int mSSesInOneObjectAmount, int channels, DelayGenerator mSSDelayGenerator)
        throws PetriObjectModelException {

        List<Transition> transitions = new ArrayList<>();

        Transition prevTransition = null;
        for (int i = 0; i < mSSesInOneObjectAmount; ++i) {

            Transition mSS = createMSS((mSSGroupId - 1) * mSSesInOneObjectAmount + i + 1, channels, mSSDelayGenerator,
                prevTransition);

            transitions.add(mSS);

            prevTransition = mSS;
        }

        return new PetriObject(mSSGroupId, "MSSGroup " + mSSGroupId, transitions);
    }

    private static Transition createMSS(int mSSId, int channels, DelayGenerator mSSDelayGenerator,
        Transition prevTransition) {

        Place queue = new Place(mSSId * 2 - 1, "Queue " + (mSSId * 2 - 1), false);
        Place limit = new Place(mSSId * 2, "Limit " + (mSSId * 2), channels, false);

        Transition mSS = new Transition(mSSId, "MSS " + mSSId, mSSDelayGenerator, false);

        mSS.addArcFrom(queue);
        mSS.addArcFrom(limit);
        mSS.addArcTo(limit);

        if (prevTransition != null) {
            prevTransition.addArcTo(queue);
        }

        return mSS;
    }
}
