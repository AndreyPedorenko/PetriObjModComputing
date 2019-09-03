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
import edu.pedorenko.petri_object_model.statistics.PetriObjectStatistics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PetriObjectModel1dot9 {

    public static void main(String[] args) throws PetriObjectModelException, ExecutionException, InterruptedException {


        Place generetorPlace = new Place(1, "Generator", 1, false);
        Place queue1 = new Place(2, "Queue1", true);
        Place limit1 = new Place(3, "Limit1", 1, true);
        Place crossroad = new Place(4, "Crossroad", false);
        Place queue2 = new Place(5, "Queue2", true);
        Place limit2 = new Place(6, "Limit2", 1, true);
        Place queue3 = new Place(7, "Queue3", true);
        Place limit3 = new Place(8, "Limit3", 1, true);
        Place queue4 = new Place(9, "Queue4", true);
        Place limit4 = new Place(10, "Limit4", 2, true);
        Place end = new Place(11, "End", false);

        Transition generator = new Transition(1, "Generator", new ExponentialDelayGenerator(2));
        Transition MSS1 = new Transition(2, "MSS1", new ExponentialDelayGenerator(0.6));
        Transition toEnd = new Transition(3, "ToEnd", 0.42d, new ConstantDelayGenerator(0));
        Transition toQueue2 = new Transition(4, "ToQueue2", 0.15d, new ConstantDelayGenerator(0));
        Transition toQueue3 = new Transition(5, "ToQueue3", 0.13d, new ConstantDelayGenerator(0));
        Transition toQueue4 = new Transition(6, "ToQueue4", 0.3d, new ConstantDelayGenerator(0));
        Transition MSS2 = new Transition(7, "MSS2", new ExponentialDelayGenerator(0.3));
        Transition MSS3 = new Transition(8, "MSS3", new ExponentialDelayGenerator(0.4));
        Transition MSS4 = new Transition(9, "MSS4", new ExponentialDelayGenerator(0.1));

        generator.addArcFrom(generetorPlace);
        generator.addArcTo(generetorPlace);
        generator.addArcTo(queue1);

        MSS1.addArcFrom(queue1);
        MSS1.addArcFrom(limit1);
        MSS1.addArcTo(limit1);
        MSS1.addArcTo(crossroad);

        toEnd.addArcFrom(crossroad);
        toEnd.addArcTo(end);

        toQueue2.addArcFrom(crossroad);
        toQueue2.addArcTo(queue2);

        toQueue3.addArcFrom(crossroad);
        toQueue3.addArcTo(queue3);

        toQueue4.addArcFrom(crossroad);
        toQueue4.addArcTo(queue4);

        MSS2.addArcFrom(queue2);
        MSS2.addArcFrom(limit2);
        MSS2.addArcTo(limit2);
        MSS2.addArcTo(queue1);

        MSS3.addArcFrom(queue3);
        MSS3.addArcFrom(limit3);
        MSS3.addArcTo(limit3);
        MSS3.addArcTo(queue1);

        MSS4.addArcFrom(queue4);
        MSS4.addArcFrom(limit4);
        MSS4.addArcTo(limit4);
        MSS4.addArcTo(queue1);

        List<Transition> transitionList = new ArrayList<>();
        transitionList.add(generator);
        transitionList.add(MSS1);
        transitionList.add(toEnd);
        transitionList.add(toQueue2);
        transitionList.add(toQueue3);
        transitionList.add(toQueue4);
        transitionList.add(MSS2);
        transitionList.add(MSS3);
        transitionList.add(MSS4);

        PetriObject petriObject = new PetriObject(1, "1.8",transitionList);
        PetriObjectModel petriObjectModel = new PetriObjectModel(new ArrayList<PetriObject>(){{add(petriObject);}});
        PetriObjectModelStatistics statistics = PetriObjectModelComputer.computeParallel(petriObjectModel, 1000000, new DummyEventProtocolFactory());

        PetriObjectStatistics petriObjectStatistics = statistics.getPetriObjectStatisticsByPetriObjectId(1);
        System.out.println("Queue1 - " + petriObjectStatistics.getPlaceStatisticsByPlaceId(2).getMeanMarking());
        System.out.println("Queue2 - " + petriObjectStatistics.getPlaceStatisticsByPlaceId(5).getMeanMarking());
        System.out.println("Queue3 - " + petriObjectStatistics.getPlaceStatisticsByPlaceId(7).getMeanMarking());
        System.out.println("Queue4 - " + petriObjectStatistics.getPlaceStatisticsByPlaceId(9).getMeanMarking());
        System.out.println("Limit1 - " + petriObjectStatistics.getPlaceStatisticsByPlaceId(3).getMeanMarking());
        System.out.println("Limit2 - " + petriObjectStatistics.getPlaceStatisticsByPlaceId(6).getMeanMarking());
        System.out.println("Limit3 - " + petriObjectStatistics.getPlaceStatisticsByPlaceId(8).getMeanMarking());
        System.out.println("Limit4 - " + petriObjectStatistics.getPlaceStatisticsByPlaceId(10).getMeanMarking());
    }
}
