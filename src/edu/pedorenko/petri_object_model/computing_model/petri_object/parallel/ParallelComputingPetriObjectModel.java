package edu.pedorenko.petri_object_model.computing_model.petri_object.parallel;

import edu.pedorenko.petri_object_model.computing_model.time.TimeState;
import edu.pedorenko.petri_object_model.statistics.PetriObjectModelStatistics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelComputingPetriObjectModel {

    private List<ParallelComputingPetriObject> petriObjects;

    private PetriObjectModelStatistics petriObjectModelStatistics;

    public ParallelComputingPetriObjectModel(List<ParallelComputingPetriObject> petriObjects, PetriObjectModelStatistics petriObjectModelStatistics) {
        this.petriObjects = petriObjects;
        this.petriObjectModelStatistics = petriObjectModelStatistics;
    }

    public PetriObjectModelStatistics go(double simulationTime) throws ExecutionException, InterruptedException {

        petriObjects.forEach(petriObject -> petriObject.setTimeState(new TimeState(simulationTime)));

        ExecutorService executorService = Executors.newWorkStealingPool();

        List<Future> futures = new ArrayList<>();
        for (ParallelComputingPetriObject petriObject : petriObjects) {
            futures.add(executorService.submit(petriObject));
        }

        for (Future future : futures) {
            future.get();
        }

        executorService.shutdown();

        return petriObjectModelStatistics;
    }
}
