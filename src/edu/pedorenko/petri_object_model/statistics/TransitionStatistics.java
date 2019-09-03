package edu.pedorenko.petri_object_model.statistics;

import edu.pedorenko.petri_object_model.computing_model.petri_object.transition.ComputingTransition;

public class TransitionStatistics {

    private ComputingTransition transition;

    private long bufferSize;

    private long maxObservedBufferSize = Long.MIN_VALUE;

    private long minObservedBufferSize = Long.MAX_VALUE;

    private double meanBufferSize;

    public TransitionStatistics(ComputingTransition transition) {
        this.transition = transition;
    }

    void doStatistics(double timeDelta) {
        bufferSize = transition.getEventsAmount();

        if (bufferSize > maxObservedBufferSize) {
            maxObservedBufferSize = bufferSize;
        }

        if (bufferSize < minObservedBufferSize) {
            minObservedBufferSize = bufferSize;
        }

        meanBufferSize = meanBufferSize + (bufferSize - meanBufferSize) * timeDelta;
    }

    public long getBufferSize() {
        return bufferSize;
    }

    public long getMaxObservedBufferSize() {
        return maxObservedBufferSize;
    }

    public long getMinObservedBufferSize() {
        return minObservedBufferSize;
    }

    public double getMeanBufferSize() {
        return meanBufferSize;
    }

    public String toString() {
        return "TransitionStatistics{" +
                "bufferSize=" + bufferSize +
                ", maxObservedBufferSize=" + maxObservedBufferSize +
                ", minObservedBufferSize=" + minObservedBufferSize +
                ", meanBufferSize=" + meanBufferSize +
                '}';
    }
}
