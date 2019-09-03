package edu.pedorenko.petri_object_model.model_computer.cycles_resolver;

import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModel;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.arc.ArcIn;
import edu.pedorenko.petri_object_model.model.arc.ArcOut;
import edu.pedorenko.petri_object_model.model.transition.Transition;
import edu.pedorenko.petri_object_model.model_computer.petri_object_merger.PetriObjectMerger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class StronglyConnectedComponentsResolver {

    private StronglyConnectedComponentsResolver() {
    }

    public static PetriObjectModel resolveStronglyConnectedComponents(PetriObjectModel petriObjectModel) throws PetriObjectModelException {

        List<List<PetriObjectGraphVertex>> stronglyConnectedComponents = findStronglyConnectedComponents(petriObjectModel);

        return createNewPetriObjectModel(stronglyConnectedComponents);
    }

    private static List<List<PetriObjectGraphVertex>> findStronglyConnectedComponents(PetriObjectModel petriObjectModel) {

        List<PetriObjectGraphVertex> graph = createGraph(petriObjectModel);

        List<List<PetriObjectGraphVertex>> stronglyConnectedComponents = new ArrayList<>();

        Set<PetriObjectGraphVertex> visitedVertex1 = new HashSet<>();
        Stack<PetriObjectGraphVertex> finishedVertex = new Stack<>();
        for (PetriObjectGraphVertex vertex : graph) {
            if (!visitedVertex1.contains(vertex)) {
                fillVisitedVertexStack(vertex, visitedVertex1, finishedVertex);
            }
        }

        Set<PetriObjectGraphVertex> visitedVertex2 = new HashSet<>();
        while (finishedVertex.size() > 0) {
            PetriObjectGraphVertex vertex = finishedVertex.pop();
            if (!visitedVertex2.contains(vertex)) {
                List<PetriObjectGraphVertex> stronglyConnectedComponent = new ArrayList<>();
                fillStronglyConnectedComponent(vertex, visitedVertex2, stronglyConnectedComponent);
                stronglyConnectedComponents.add(stronglyConnectedComponent);
            }
        }

        return stronglyConnectedComponents;
    }

    private static void fillVisitedVertexStack(
            PetriObjectGraphVertex vertex,
            Set<PetriObjectGraphVertex> visitedVertex,
            Stack<PetriObjectGraphVertex> finishedVertex) {

        visitedVertex.add(vertex);

        for (PetriObjectGraphVertex child : vertex.getChildren()) {
            if (!visitedVertex.contains(child)) {
                fillVisitedVertexStack(child, visitedVertex, finishedVertex);
            }
        }

        finishedVertex.push(vertex);
    }

    private static void fillStronglyConnectedComponent(
            PetriObjectGraphVertex vertex,
            Set<PetriObjectGraphVertex> visitedVertex,
            List<PetriObjectGraphVertex> stronglyConnectedComponent) {

        visitedVertex.add(vertex);
        stronglyConnectedComponent.add(vertex);

        for (PetriObjectGraphVertex reversedChild : vertex.getReversedChildren()) {
            if (!visitedVertex.contains(reversedChild)) {
                fillStronglyConnectedComponent(reversedChild, visitedVertex, stronglyConnectedComponent);
            }
        }
    }

    private static List<PetriObjectGraphVertex> createGraph(PetriObjectModel petriObjectModel) {

        List<PetriObjectGraphVertex> graph = new ArrayList<>();

        Map<PetriObject, PetriObjectGraphVertex> petriObjectToVertexMap = new HashMap<>();

        for (PetriObject petriObject : petriObjectModel) {
            PetriObjectGraphVertex petriObjectGraphVertex = new PetriObjectGraphVertex(petriObject);
            graph.add(petriObjectGraphVertex);
            petriObjectToVertexMap.put(petriObject, petriObjectGraphVertex);
        }

        for (PetriObjectGraphVertex petriObjectGraphVertex : graph) {

            PetriObject petriObject = petriObjectGraphVertex.getPetriObject();

            for (Transition transition : petriObject) {

                for (ArcOut arcOut : transition.getArcsOut()) {

                    PetriObject placePetriObject = arcOut.getPlace().getPetriObject();

                    if (petriObject != placePetriObject) {

                        PetriObjectGraphVertex child = petriObjectToVertexMap.get(placePetriObject);
                        petriObjectGraphVertex.addChild(child);
                        child.addReversedChild(petriObjectGraphVertex);

                    }
                }

                for (ArcIn arcIn : transition.getArcsIn()) {

                    PetriObject placePetriObject = arcIn.getPlace().getPetriObject();

                    if (petriObject != placePetriObject) {

                        PetriObjectGraphVertex child = petriObjectToVertexMap.get(placePetriObject);
                        petriObjectGraphVertex.addChild(child);
                        child.addReversedChild(petriObjectGraphVertex);

                        child.addChild(petriObjectGraphVertex);//common places are interpreted as cycles
                        petriObjectGraphVertex.addReversedChild(child);
                    }
                }
            }
        }

        return graph;
    }

    private static PetriObjectModel createNewPetriObjectModel(List<List<PetriObjectGraphVertex>> stronglyConnectedComponents) throws PetriObjectModelException {

        List<PetriObject> petriObjects = new ArrayList<>();
        for (List<PetriObjectGraphVertex> stronglyConnectedComponent : stronglyConnectedComponents) {

            List<PetriObject> stronglyConnectedPetriObjects = stronglyConnectedComponent.stream()
                    .map(PetriObjectGraphVertex::getPetriObject)
                    .collect(Collectors.toList());

            if (stronglyConnectedPetriObjects.size() > 1) {
                PetriObject mergedPetriObject = PetriObjectMerger.mergePetriObjects(stronglyConnectedPetriObjects);
                petriObjects.add(mergedPetriObject);
            } else {
                petriObjects.add(stronglyConnectedPetriObjects.get(0));
            }
        }

        return new PetriObjectModel(petriObjects);
    }


}
