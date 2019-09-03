package edu.pedorenko.petri_object_model.model_computer;

import edu.pedorenko.petri_object_model.computing_model.petri_object.arc.ComputingArcIn;
import edu.pedorenko.petri_object_model.computing_model.petri_object.arc.ComputingArcOut;
import edu.pedorenko.petri_object_model.computing_model.petri_object.arc.ComputingInformationalArcIn;
import edu.pedorenko.petri_object_model.computing_model.petri_object.parallel.ParallelComputingPetriObject;
import edu.pedorenko.petri_object_model.computing_model.petri_object.parallel.ParallelComputingPetriObjectModel;
import edu.pedorenko.petri_object_model.computing_model.petri_object.parallel.arc.ComputingExternalArcOut;
import edu.pedorenko.petri_object_model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petri_object_model.computing_model.petri_object.sequentional.SequentialComputingPetriObject;
import edu.pedorenko.petri_object_model.computing_model.petri_object.sequentional.SequentialComputingPetriObjectModel;
import edu.pedorenko.petri_object_model.computing_model.petri_object.transition.ComputingTransition;
import edu.pedorenko.petri_object_model.event_protocol.EventProtocol;
import edu.pedorenko.petri_object_model.event_protocol.EventProtocolFactory;
import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModel;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.arc.ArcIn;
import edu.pedorenko.petri_object_model.model.arc.ArcOut;
import edu.pedorenko.petri_object_model.model.arc.InformationalArcIn;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.DelayGenerator;
import edu.pedorenko.petri_object_model.model_computer.cycles_resolver.StronglyConnectedComponentsResolver;
import edu.pedorenko.petri_object_model.statistics.PetriObjectModelStatistics;
import edu.pedorenko.petri_object_model.statistics.PetriObjectStatistics;
import edu.pedorenko.petri_object_model.statistics.PlaceStatistics;
import edu.pedorenko.petri_object_model.statistics.TransitionStatistics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class PetriObjectModelComputer {

    public static PetriObjectModelStatistics computeSequential(
            PetriObjectModel petriObjectModel,
            double timeModeling,
            EventProtocol eventProtocol) {

        SequentialComputingPetriObjectModel computingPetriObjectModel =
                createSequentialPetriObjectModel(petriObjectModel, eventProtocol);

        return computingPetriObjectModel.go(timeModeling);
    }

    public static PetriObjectModelStatistics computeParallel(
            PetriObjectModel petriObjectModel,
            double timeModeling,
            EventProtocolFactory eventProtocolFactory) throws ExecutionException, InterruptedException, PetriObjectModelException {

        petriObjectModel = StronglyConnectedComponentsResolver.resolveStronglyConnectedComponents(petriObjectModel);

        ParallelComputingPetriObjectModel computingPetriObjectModel
                = createParallelComputingPetriObjectModel(petriObjectModel, eventProtocolFactory);

        return computingPetriObjectModel.go(timeModeling);
    }

    private static SequentialComputingPetriObjectModel createSequentialPetriObjectModel(PetriObjectModel petriObjectModel, EventProtocol eventProtocol) {

        PetriObjectModelStatistics petriObjectModelStatistics = new PetriObjectModelStatistics();

        List<SequentialComputingPetriObject> sequentialComputingPetriObjects =
                createSequentialPetriObjects(petriObjectModel, eventProtocol, petriObjectModelStatistics);

        return new SequentialComputingPetriObjectModel(
                sequentialComputingPetriObjects,
                eventProtocol,
                petriObjectModelStatistics);
    }

    private static List<SequentialComputingPetriObject> createSequentialPetriObjects(
            PetriObjectModel petriObjectModel,
            EventProtocol eventProtocol,
            PetriObjectModelStatistics petriObjectModelStatistics) {

        Map<Place, ComputingPlace> allComputingPlacesMap = new HashMap<>();
        List<SequentialComputingPetriObject> sequentialComputingPetriObjects = new ArrayList<>();

        for (PetriObject petriObject : petriObjectModel) {

            PetriObjectStatistics petriObjectStatistics = new PetriObjectStatistics();

            Set<ComputingPlace> thisObjectComputingPlaces = new LinkedHashSet<>();

            List<ComputingTransition> computingTransitions =
                    createSequentialComputingTransitionsAndFillPlacesSet(
                            petriObject,
                            allComputingPlacesMap,
                            thisObjectComputingPlaces,
                            petriObjectStatistics);

            List<Long> petriObjectIds = petriObject.getPetriObjectIds();
            String petriObjectName = petriObject.getPetriObjectName();
            int priority = petriObject.getPriority();

            SequentialComputingPetriObject computingPetriObject =
                    new SequentialComputingPetriObject(
                            petriObjectIds,
                            petriObjectName,
                            computingTransitions,
                            new ArrayList<>(thisObjectComputingPlaces),
                            priority,
                            eventProtocol);

            sequentialComputingPetriObjects.add(computingPetriObject);

            if (petriObjectStatistics.hasStatistics()) {
                for (Long petriObjectId : petriObjectIds) {
                    petriObjectModelStatistics.putPetriObjectStatistics(petriObjectId, petriObjectStatistics);
                }
            }
        }

        return sequentialComputingPetriObjects;
    }

    private static List<ComputingTransition> createSequentialComputingTransitionsAndFillPlacesSet(
            PetriObject petriObject,
            Map<Place, ComputingPlace> allComputingPlacesMap,
            Set<ComputingPlace> thisObjectsComputingPlaces,
            PetriObjectStatistics petriObjectStatistics) {

        List<ComputingTransition> computingTransitions = new ArrayList<>();

        for (Transition transition : petriObject) {

            long transitionId = transition.getTransitionId();
            String transitionName = transition.getTransitionName();
            int priority = transition.getPriority();
            double probability = transition.getProbability();
            DelayGenerator delayGenerator = transition.getDelayGenerator();
            ComputingTransition computingTransition = new ComputingTransition(transitionId, transitionName, priority, probability, delayGenerator);

            addSequentialArcsAndFillPlacesSet(
                    computingTransition,
                    transition,
                    allComputingPlacesMap,
                    thisObjectsComputingPlaces,
                    petriObjectStatistics);

            computingTransitions.add(computingTransition);

            if (transition.isCollectStatistics()) {
                petriObjectStatistics.putTransitionStatistics(transitionId, new TransitionStatistics(computingTransition));
            }
        }

        return computingTransitions;
    }

    private static void addSequentialArcsAndFillPlacesSet(
            ComputingTransition computingTransition,
            Transition transition,
            Map<Place, ComputingPlace> allComputingPlacesMap,
            Set<ComputingPlace> thisObjectsComputingPlaces,
            PetriObjectStatistics petriObjectStatistics) {

        for (ArcIn arcIn : transition.getArcsIn()) {

            Place place = arcIn.getPlace();
            int multiplicity = arcIn.getMultiplicity();

            ComputingPlace computingPlace = createComputingPlace(place, allComputingPlacesMap);
            if (place.getPetriObject() == transition.getPetriObject() && !thisObjectsComputingPlaces.contains(computingPlace)) {
                thisObjectsComputingPlaces.add(computingPlace);
                if (place.isCollectStatistics()) {
                    petriObjectStatistics.putPlaceStatistics(place.getPlaceId(), new PlaceStatistics(computingPlace));
                }
            }

            if (arcIn instanceof InformationalArcIn) {
                computingTransition.addArcIn(
                        new ComputingInformationalArcIn(computingPlace, computingTransition, multiplicity));
            } else {
                computingTransition.addArcIn(
                        new ComputingArcIn(computingPlace, computingTransition, multiplicity));
            }
        }

        for (ArcOut arcOut : transition.getArcsOut()) {

            Place place = arcOut.getPlace();
            int multiplicity = arcOut.getMultiplicity();

            ComputingPlace computingPlace = createComputingPlace(place, allComputingPlacesMap);
            if (place.getPetriObject() == transition.getPetriObject() && !thisObjectsComputingPlaces.contains(computingPlace)) {
                thisObjectsComputingPlaces.add(computingPlace);
                if (place.isCollectStatistics()) {
                    petriObjectStatistics.putPlaceStatistics(place.getPlaceId(), new PlaceStatistics(computingPlace));
                }
            }
            computingTransition.addArcOut(new ComputingArcOut(computingTransition, computingPlace, multiplicity));
        }
    }

    private static ComputingPlace createComputingPlace(Place place, Map<Place, ComputingPlace> allComputingPlacesMap) {

        ComputingPlace computingPlace;
        if (allComputingPlacesMap.containsKey(place)) {
            return allComputingPlacesMap.get(place);
        } else {
            long placeId = place.getPlaceId();
            String placeName = place.getPlaceName();
            int initialMarking = place.getMarking();
            computingPlace = new ComputingPlace(placeId, placeName, initialMarking);
            allComputingPlacesMap.put(place, computingPlace);
            return computingPlace;
        }
    }

    private static ParallelComputingPetriObjectModel createParallelComputingPetriObjectModel(PetriObjectModel petriObjectModel, EventProtocolFactory eventProtocolFactory) {

        PetriObjectModelStatistics petriObjectModelStatistics = new PetriObjectModelStatistics();

        List<ParallelComputingPetriObject> parallelComputingPetriObjects
                = createParallelComputingPetriObjects(petriObjectModel, eventProtocolFactory, petriObjectModelStatistics);

        return new ParallelComputingPetriObjectModel(parallelComputingPetriObjects, petriObjectModelStatistics);
    }

    private static List<ParallelComputingPetriObject> createParallelComputingPetriObjects(
            PetriObjectModel petriObjectModel,
            EventProtocolFactory eventProtocolFactory,
            PetriObjectModelStatistics petriObjectModelStatistics) {

        Map<Place, ComputingPlace> computingPlacesMap = new HashMap<>();
        Map<PetriObject, List<ComputingExternalArcOut>> nextObjectComputingExternalArcsOut = new HashMap<>();

        List<ParallelComputingPetriObject> parallelComputingPetriObjects = new ArrayList<>();

        for (PetriObject petriObject : petriObjectModel) {
            nextObjectComputingExternalArcsOut.put(petriObject, new ArrayList<>());
        }

        for (PetriObject petriObject : petriObjectModel) {

            PetriObjectStatistics petriObjectStatistics = new PetriObjectStatistics();

            Set<ComputingPlace> thisObjectComputingPlaces = new LinkedHashSet<>();

            List<CreateComputingExternalArcTask> createComputingExternalArcTasks = new ArrayList<>();

            List<ComputingTransition> computingTransitions = createParallelComputingTransitionsAndFillPlacesSet(
                    petriObject,
                    computingPlacesMap,
                    thisObjectComputingPlaces,
                    createComputingExternalArcTasks,
                    petriObjectStatistics);

            List<ComputingExternalArcOut> prevObjectsExternalArcsOut = nextObjectComputingExternalArcsOut.get(petriObject);

            List<Long> petriObjectIds = petriObject.getPetriObjectIds();
            String petriObjectName = petriObject.getPetriObjectName();
            int externalBufferSizeLimit = petriObject.getExternalBufferSizeLimit();

            ParallelComputingPetriObject computingPetriObject =
                    new ParallelComputingPetriObject(
                            petriObjectIds,
                            petriObjectName,
                            computingTransitions,
                            new ArrayList<>(thisObjectComputingPlaces),
                            prevObjectsExternalArcsOut,
                            externalBufferSizeLimit,
                            eventProtocolFactory.createEventProtocol(),
                            petriObjectStatistics);

            for (CreateComputingExternalArcTask createComputingExternalArcTask : createComputingExternalArcTasks) {

                ComputingTransition transition = createComputingExternalArcTask.getTransition();
                ComputingPlace place = createComputingExternalArcTask.getPlace();
                int multiplicity = createComputingExternalArcTask.getMultiplicity();

                ComputingExternalArcOut externalArcOut = new ComputingExternalArcOut(computingPetriObject, transition, place, multiplicity);

                transition.addArcOut(externalArcOut);
                computingPetriObject.addExternalArcOut(externalArcOut);

                PetriObject nextPetriObject = createComputingExternalArcTask.getNextPetriObject();
                nextObjectComputingExternalArcsOut.get(nextPetriObject).add(externalArcOut);
            }

            parallelComputingPetriObjects.add(computingPetriObject);

            if (petriObjectStatistics.hasStatistics()) {
                for (Long petriObjectId : petriObjectIds) {
                    petriObjectModelStatistics.putPetriObjectStatistics(petriObjectId, petriObjectStatistics);
                }
            }
        }

        return parallelComputingPetriObjects;
    }

    private static List<ComputingTransition> createParallelComputingTransitionsAndFillPlacesSet(
            PetriObject petriObject,
            Map<Place, ComputingPlace> computingPlacesMap,
            Set<ComputingPlace> thisObjectComputingPlaces,
            List<CreateComputingExternalArcTask> createComputingExternalArcTasks,
            PetriObjectStatistics petriObjectStatistics) {

        List<ComputingTransition> computingTransitions = new ArrayList<>();

        for (Transition transition : petriObject) {

            long transitionId = transition.getTransitionId();
            String transitionName = transition.getTransitionName();
            int priority = transition.getPriority();
            double probability = transition.getProbability();
            DelayGenerator delayGenerator = transition.getDelayGenerator();
            ComputingTransition computingTransition = new ComputingTransition(transitionId, transitionName, priority, probability, delayGenerator);

            addParallelArcsAndFillPlacesSet(
                    computingTransition,
                    transition,
                    computingPlacesMap,
                    thisObjectComputingPlaces,
                    createComputingExternalArcTasks,
                    petriObjectStatistics);

            computingTransitions.add(computingTransition);

            if (transition.isCollectStatistics()) {
                petriObjectStatistics.putTransitionStatistics(transitionId, new TransitionStatistics(computingTransition));
            }
        }

        return computingTransitions;
    }

    private static void addParallelArcsAndFillPlacesSet(
            ComputingTransition computingTransition,
            Transition transition,
            Map<Place, ComputingPlace> computingPlacesMap,
            Set<ComputingPlace> thisObjectComputingPlaces,
            List<CreateComputingExternalArcTask> createComputingExternalArcTasks,
            PetriObjectStatistics petriObjectStatistics) {

        for (ArcIn arcIn : transition.getArcsIn()) {

            Place place = arcIn.getPlace();

            if (place.getPetriObject() != transition.getPetriObject()) {//compare by link
                throw new IllegalStateException("Only external arcs out allowed in parallel Petri object model.\n" +
                        "External arc in: " + arcIn);
            }

            int multiplicity = arcIn.getMultiplicity();

            ComputingPlace computingPlace = createComputingPlace(place, computingPlacesMap);
            if (place.getPetriObject() == transition.getPetriObject() && !thisObjectComputingPlaces.contains(computingPlace)) {
                thisObjectComputingPlaces.add(computingPlace);
                if (place.isCollectStatistics()) {
                    petriObjectStatistics.putPlaceStatistics(place.getPlaceId(), new PlaceStatistics(computingPlace));
                }
            }

            if (arcIn instanceof InformationalArcIn) {
                computingTransition.addArcIn(
                        new ComputingInformationalArcIn(computingPlace, computingTransition, multiplicity));
            } else {
                computingTransition.addArcIn(
                        new ComputingArcIn(computingPlace, computingTransition, multiplicity));
            }
        }

        for (ArcOut arcOut : transition.getArcsOut()) {

            Place place = arcOut.getPlace();
            int multiplicity = arcOut.getMultiplicity();

            ComputingPlace computingPlace = createComputingPlace(place, computingPlacesMap);

            if (place.getPetriObject() == transition.getPetriObject()) {//compare by link

                if (!thisObjectComputingPlaces.contains(computingPlace)) {
                    thisObjectComputingPlaces.add(computingPlace);
                    if (place.isCollectStatistics()) {
                        petriObjectStatistics.putPlaceStatistics(place.getPlaceId(), new PlaceStatistics(computingPlace));
                    }
                }

                computingTransition.addArcOut(new ComputingArcOut(computingTransition, computingPlace, multiplicity));

            } else {

                PetriObject nextPetriObject = place.getPetriObject();

                createComputingExternalArcTasks.add(new CreateComputingExternalArcTask(
                        nextPetriObject,
                        computingTransition,
                        computingPlace,
                        multiplicity));
            }
        }
    }
}
