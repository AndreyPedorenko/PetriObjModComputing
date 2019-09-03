package edu.pedorenko.petri_object_model.computing_model.time;

public class TimeState {

    private double currentTime;
    private double simulationTime;

    public TimeState(double simulationTime) {
        this.currentTime = 0;
        this.simulationTime = simulationTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public double getSimulationTime() {
        return simulationTime;
    }
}
