package edu.pedorenko.petri_object_model.model.transition.delay_generator;

public class ExponentialDelayGenerator implements DelayGenerator {

    private double meanDelay;

    public ExponentialDelayGenerator(double meanDelay) {

        if (meanDelay <= 0) {
            throw new IllegalArgumentException("Mean delay should be more then zero. meanDelay = " + meanDelay);
        }

        this.meanDelay = meanDelay;
    }

    public Double get() {
        return FunctionalRandom.exp(meanDelay);
    }
}
