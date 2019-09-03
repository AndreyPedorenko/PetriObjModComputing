package edu.pedorenko.petri_object_model.computing_model.petri_object.parallel.arc;

import edu.pedorenko.petri_object_model.computing_model.petri_object.arc.ComputingArcOut;
import edu.pedorenko.petri_object_model.computing_model.petri_object.parallel.ParallelComputingPetriObject;
import edu.pedorenko.petri_object_model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petri_object_model.computing_model.petri_object.transition.ComputingTransition;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ComputingExternalArcOut extends ComputingArcOut {

    private Lock lock = new ReentrantLock();
    private Condition emptyBufferCondition = lock.newCondition();

    private List<Double> eventTimesBuffer = new ArrayList<>();

    private ParallelComputingPetriObject prevObject;

    public ComputingExternalArcOut(
            ParallelComputingPetriObject prevObject,
            ComputingTransition transition,
            ComputingPlace place,
            int multiplicity) {

        super(transition, place, multiplicity);

        this.prevObject = prevObject;
    }

    public ComputingExternalArcOut(
            ParallelComputingPetriObject prevObject,
            ComputingTransition transition,
            ComputingPlace place) {

        this(prevObject, transition, place, 1);
    }

    public double getNearestEventTime() {

        try {
            lock.lock();

            while (eventTimesBuffer.isEmpty()) {
                emptyBufferCondition.await();
            }

            return eventTimesBuffer.get(0);

        } catch (InterruptedException ex) {

            throw new RuntimeException("Exception occurred while waiting on emptyBufferCondition", ex);

        } finally {
            lock.unlock();
        }
    }

    public void runArc(double currentTime) {

        try {
            lock.lock();
            eventTimesBuffer.add(currentTime);
            emptyBufferCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void actOut(double currentTime) {

        try {
            lock.lock();

            if (currentTime != eventTimesBuffer.get(0)) {
                return;
            }

            eventTimesBuffer.remove(0);

        } finally {
            lock.unlock();
        }

        place.increaseMarking(multiplicity);

        prevObject.signalEventTakenFromNextObjectBuffer();
    }

    public int getBufferSize() {
        try {
            lock.lock();
            return eventTimesBuffer.size();
        } finally {
            lock.unlock();
        }
    }
}
