package edu.pedorenko.petri_object_model.model.transition.delay_generator;

public class NormalDelayGenerator implements DelayGenerator {

    private double meanDelay;

    private double delayDeviation;

    public NormalDelayGenerator(double meanDelay, double delayDeviation) {

        if (delayDeviation <= 0) {
            throw new IllegalArgumentException("Delay deviation should be more then zero. delayDeviation = " + delayDeviation);
        }

        this.meanDelay = meanDelay;
        this.delayDeviation = delayDeviation;
    }

    public Double get() {

        double delay;
        do {
            delay = FunctionalRandom.norm(meanDelay, delayDeviation);
        } while (delay < 0);

        return delay;
    }
}
