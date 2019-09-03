package edu.pedorenko.petri_object_model.model_computer.petri_object_merger;

import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.arc.ArcIn;
import edu.pedorenko.petri_object_model.model.arc.ArcOut;
import edu.pedorenko.petri_object_model.model.arc.InformationalArcIn;
import edu.pedorenko.petri_object_model.model.place.Place;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PetriObjectMerger {

    private PetriObjectMerger() {
    }

    public static PetriObject mergePetriObjects(List<PetriObject> petriObjects) throws PetriObjectModelException {

        PetriObject firstPetriObject = petriObjects.get(0);
        List<Long> petriObjectIds = new ArrayList<>(firstPetriObject.getPetriObjectIds());
        StringBuilder petriObjectNameSB = new StringBuilder(firstPetriObject.getPetriObjectName());
        List<Transition> transitions = new ArrayList<>();
        int priority = firstPetriObject.getPriority();
        int externalBufferSizeLimit = firstPetriObject.getExternalBufferSizeLimit();

        List<CreateArcInTask> createArcInTasks = new ArrayList<>();
        List<CreateArcOutTask> createArcOutTasks = new ArrayList<>();

        for (PetriObject petriObject : petriObjects) {
            for (Place place : petriObject.getThisObjectPlaces()) {
                place.resetPetriObject();
            }
        }

        addPetriObjectTransitions(firstPetriObject, transitions, createArcInTasks, createArcOutTasks);

        for (int i = 1; i < petriObjects.size(); ++i) {
            PetriObject petriObject = petriObjects.get(i);

            petriObjectIds.addAll(petriObject.getPetriObjectIds());
            petriObjectNameSB.append("+").append(petriObject.getPetriObjectName());
            if (priority < petriObject.getPriority()) {
                priority = petriObject.getPriority();
            }
            if (externalBufferSizeLimit > petriObject.getExternalBufferSizeLimit()) {
                externalBufferSizeLimit = petriObject.getExternalBufferSizeLimit();
            }

            addPetriObjectTransitions(petriObject, transitions, createArcInTasks, createArcOutTasks);
        }

        PetriObject mergedPetriObject = new PetriObject(
                petriObjectIds,
                petriObjectNameSB.toString(),
                transitions,
                priority,
                externalBufferSizeLimit);

        createArcInTasks.forEach(CreateArcInTask::doTask);
        createArcOutTasks.forEach(CreateArcOutTask::doTask);

        return mergedPetriObject;
    }

    private static void addPetriObjectTransitions(
            PetriObject petriObject,
            List<Transition> transitions,
            List<CreateArcInTask> createArcInTasks,
            List<CreateArcOutTask> createArcOutTasks) {

        for (Transition transition : petriObject) {
            transition.resetPetriObject();

            Iterator<ArcIn> arcInIternator = transition.getArcsIn().iterator();
            while (arcInIternator.hasNext()) {
                ArcIn arcIn = arcInIternator.next();
                if (arcIn.getPlace().getPetriObject() != null) {

                    Place place = arcIn.getPlace();
                    int multiplicity = arcIn.getMultiplicity();
                    boolean isInformational = arcIn instanceof InformationalArcIn;

                    createArcInTasks.add(new CreateArcInTask(transition, place, multiplicity, isInformational));

                    arcInIternator.remove();
                }
            }

            Iterator<ArcOut> arcOutIterator = transition.getArcsOut().iterator();
            while (arcOutIterator.hasNext()) {
                ArcOut arcOut = arcOutIterator.next();
                if (arcOut.getPlace().getPetriObject() != null) {

                    Place place = arcOut.getPlace();
                    int multiplicity = arcOut.getMultiplicity();

                    createArcOutTasks.add(new CreateArcOutTask(transition, place, multiplicity));

                    arcOutIterator.remove();
                }
            }

            transitions.add(transition);
        }
    }
}
