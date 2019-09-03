package edu.pedorenko.petri_object_model.model.transition.delay_generator;

public class UniformDelayGenerator implements DelayGenerator {

    private double minDelay;

    private double maxDelay;

    public UniformDelayGenerator(double minDelay, double maxDelay) {

        if (minDelay < 0) {
            throw new IllegalArgumentException("Min delay can't be less then zero. minDelay = " + minDelay);
        }

        if (minDelay > maxDelay) {
            throw new IllegalArgumentException("Min delay can't be more then max delay. minDelay = " + minDelay + ", " +
                    "maxDelay = " + maxDelay);
        }

        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
    }

    public Double get() {
        return FunctionalRandom.unif(minDelay, maxDelay);
    }
}
