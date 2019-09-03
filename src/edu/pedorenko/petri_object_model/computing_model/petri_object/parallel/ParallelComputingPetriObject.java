package edu.pedorenko.petri_object_model.computing_model.petri_object.parallel;

import edu.pedorenko.petri_object_model.computing_model.petri_object.parallel.arc.ComputingExternalArcOut;
import edu.pedorenko.petri_object_model.computing_model.petri_object.place.ComputingPlace;
import edu.pedorenko.petri_object_model.computing_model.petri_object.sequentional.SequentialComputingPetriObject;
import edu.pedorenko.petri_object_model.computing_model.petri_object.transition.ComputingTransition;
import edu.pedorenko.petri_object_model.computing_model.time.TimeState;
import edu.pedorenko.petri_object_model.event_protocol.EventProtocol;
import edu.pedorenko.petri_object_model.event_protocol.event.TimeMovedEvent;
import edu.pedorenko.petri_object_model.statistics.PetriObjectStatistics;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ParallelComputingPetriObject extends SequentialComputingPetriObject implements Runnable {

    private PetriObjectStatistics petriObjectStatistics;

    private final Lock lock = new ReentrantLock();
    private final Condition limitEventsCondition = lock.newCondition();
    private int externalBufferSizeLimit;

    private TimeState timeState;

    private List<ComputingExternalArcOut> prevObjectsExternalArcsOut;
    private List<ComputingExternalArcOut> nextObjectsExternalArcsOut = new ArrayList<>();

    private double nearestExternalEventTime;

    public ParallelComputingPetriObject(
            List<Long> petriObjectIds,
            String name,
            List<ComputingTransition> transitions,
            List<ComputingPlace> places,
            List<ComputingExternalArcOut> prevObjectsExternalArcsOut,
            int externalBufferSizeLimit,
            EventProtocol eventProtocol, PetriObjectStatistics petriObjectStatistics) {

        super(petriObjectIds, name, transitions, places, 0, eventProtocol);

        this.prevObjectsExternalArcsOut = prevObjectsExternalArcsOut;

        this.externalBufferSizeLimit = externalBufferSizeLimit;

        this.petriObjectStatistics = petriObjectStatistics;
    }

    public void run() {

        if (timeState == null) {
            throw new RuntimeException("Set time state before running petri object.");
        }

        actIn(timeState.getCurrentTime());

        while (timeState.getCurrentTime() <= timeState.getSimulationTime()) {

            doStatisticsAndMoveTime();

            if (timeState.getCurrentTime() <= timeState.getSimulationTime()) {

                actExternalOut(timeState.getCurrentTime());

                actOut(timeState.getCurrentTime());

                awaitIfReachedEventLimit();

                actIn(timeState.getCurrentTime());
            }
        }

        nextObjectsExternalArcsOut.forEach(externalArcOut -> externalArcOut.runArc(Double.MAX_VALUE));
    }

    private void doStatisticsAndMoveTime() {

        double previousTime = timeState.getCurrentTime();

        defineNearestExternalEventTime();
        double newTime = Math.min(nearestEventTime, nearestExternalEventTime);

        if (newTime < timeState.getSimulationTime()) {
            petriObjectStatistics.doStatistics((newTime - previousTime) / newTime);
        } else {
            petriObjectStatistics.doStatistics((timeState.getSimulationTime() - previousTime) / timeState.getSimulationTime());
        }

        timeState.setCurrentTime(newTime);

        if (eventProtocol.isEnabled()) {
            TimeMovedEvent timeMovedEvent = new TimeMovedEvent(previousTime, newTime);
            eventProtocol.onTimeMovedEvent(timeMovedEvent);
        }
    }

    private void defineNearestExternalEventTime() {

        OptionalDouble optionalNearestExternalEventTime = prevObjectsExternalArcsOut.stream()
                .mapToDouble(ComputingExternalArcOut::getNearestEventTime) //Thread locks here to define safe interval
                .min();

        if (optionalNearestExternalEventTime.isPresent()) {
            nearestExternalEventTime = optionalNearestExternalEventTime.getAsDouble();
        } else {
            nearestExternalEventTime = Double.MAX_VALUE;
        }
    }

    private void actExternalOut(double currentTime) {

        prevObjectsExternalArcsOut.stream()
                .filter(externalArcOut -> externalArcOut.getNearestEventTime() == nearestExternalEventTime)
                .forEach(externalArcOut -> externalArcOut.actOut(currentTime));
    }

    private void awaitIfReachedEventLimit() {

        if (getMinExternalBufferSize() >= externalBufferSizeLimit) {

            try {
                lock.lock();
                while (getMinExternalBufferSize() >= externalBufferSizeLimit) {
                    limitEventsCondition.await();
                }

            } catch (InterruptedException ex) {
                throw new RuntimeException("Exception occurred while waiting on limitEventsCondition", ex);
            } finally {
                lock.unlock();
            }
        }
    }

    public void signalEventTakenFromNextObjectBuffer() {
        try {
            lock.lock();
            limitEventsCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    private int getMinExternalBufferSize() {

        return nextObjectsExternalArcsOut.stream()
                .mapToInt(ComputingExternalArcOut::getBufferSize)
                .min()
                .orElse(0);
    }

    void setTimeState(TimeState timeState) {

        if (this.timeState != null) {
            throw new RuntimeException("Time state is already set");
        }

        this.timeState = timeState;
    }

    public void addExternalArcOut(ComputingExternalArcOut externalArcOut) {
        nextObjectsExternalArcsOut.add(externalArcOut);
    }
}
