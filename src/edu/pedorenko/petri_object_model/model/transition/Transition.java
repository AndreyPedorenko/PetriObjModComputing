package edu.pedorenko.petri_object_model.model.transition;

import edu.pedorenko.petri_object_model.model.PetriObject;
import edu.pedorenko.petri_object_model.model.PetriObjectModelException;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.ConstantDelayGenerator;
import edu.pedorenko.petri_object_model.model.transition.delay_generator.DelayGenerator;
import edu.pedorenko.petri_object_model.model.arc.ArcIn;
import edu.pedorenko.petri_object_model.model.arc.ArcOut;
import edu.pedorenko.petri_object_model.model.arc.InformationalArcIn;
import edu.pedorenko.petri_object_model.model.place.Place;
import java.util.ArrayList;
import java.util.List;

public class Transition {

    private long transitionId;

    private String transitionName;

    private int priority;
    private double probability;

    private DelayGenerator delayGenerator;

    private List<ArcIn> arcsIn = new ArrayList<>();
    private List<ArcOut> arcsOut = new ArrayList<>();

    private PetriObject petriObject;

    private boolean collectStatistics;

    public Transition(
            long transitionId,
            String transitionName,
            int priority,
            double probability,
            DelayGenerator delayGenerator,
            boolean collectStatistics) {

        this.transitionId = transitionId;
        this.transitionName = transitionName;
        this.priority = priority;
        this.probability = probability;
        this.delayGenerator = delayGenerator;
        this.collectStatistics = collectStatistics;
    }

    public Transition(
            long transitionId,
            String transitionName,
            int priority,
            double probability,
            DelayGenerator delayGenerator) {

        this(
                transitionId,
                transitionName,
                priority,
                probability,
                delayGenerator,
                true);
    }

    public Transition(
            long transitionId,
            String transitionName,
            int priority,
            double probability,
            boolean collectStatistics) {

        this(
                transitionId,
                transitionName,
                priority,
                probability,
                new ConstantDelayGenerator(0),
                collectStatistics);

    }

    public Transition(
            long transitionId,
            String transitionName,
            int priority,
            DelayGenerator delayGenerator,
            boolean collectStatistics) {

        this(
                transitionId, transitionName,
                priority,
                1d,
                delayGenerator,
                collectStatistics);
    }

    public Transition(
            long transitionId,
            String transitionName,
            double probability,
            DelayGenerator delayGenerator,
            boolean collectStatistics) {

        this(
                transitionId,
                transitionName,
                0,
                probability,
                delayGenerator,
                collectStatistics);
    }

    public Transition(
            long transitionId,
            String transitionName,
            int priority,
            double probability) {

        this(
                transitionId,
                transitionName,
                priority,
                probability,
                new ConstantDelayGenerator(0));

    }

    public Transition(
            long transitionId,
            String transitionName,
            int priority,
            DelayGenerator delayGenerator) {

        this(
                transitionId, transitionName,
                priority,
                1d,
                delayGenerator);
    }

    public Transition(
            long transitionId,
            String transitionName,
            double probability,
            DelayGenerator delayGenerator) {

        this(
                transitionId,
                transitionName,
                0,
                probability,
                delayGenerator);
    }

    public Transition(long transitionId, String transitionName, int priority) {

        this(transitionId, transitionName, priority, 1d);
    }

    public Transition(long transitionId, String transitionName, double probability) {

        this(transitionId, transitionName, 0, probability);
    }

    public Transition(long transitionId, String transitionName, DelayGenerator delayGenerator, boolean collectStatistics) {

        this(transitionId, transitionName, 0, delayGenerator, collectStatistics);
    }

    public Transition(long transitionId, String transitionName, DelayGenerator delayGenerator) {

        this(transitionId, transitionName, 0, delayGenerator);
    }

    public Transition(long transitionId, String transitionName) {
        this(transitionId, transitionName, 0);
    }

    public void addArcFrom(Place place, int multiplicity) {
        arcsIn.add(new ArcIn(place, this, multiplicity));
    }

    public void addArcFrom(Place place) {
        addArcFrom(place, 1);
    }

    public void addInformationalArcFrom(Place place, int multiplicity) {
        arcsIn.add(new InformationalArcIn(place, this, multiplicity));
    }

    public void addInformationalArcFrom(Place place) {
        addInformationalArcFrom(place, 1);
    }

    public void addArcTo(Place place, int multiplicity) {
        arcsOut.add(new ArcOut(this, place, multiplicity));
    }

    public void addArcTo(Place place) {
        addArcTo(place, 1);
    }

    public long getTransitionId() {
        return transitionId;
    }

    public String getTransitionName() {
        return transitionName;
    }

    public int getPriority() {
        return priority;
    }

    public double getProbability() {
        return probability;
    }

    public DelayGenerator getDelayGenerator() {
        return delayGenerator;
    }

    public List<ArcIn> getArcsIn() {
        return arcsIn;
    }

    public List<ArcOut> getArcsOut() {
        return arcsOut;
    }

    public PetriObject getPetriObject() {
        return petriObject;
    }

    public void setPetriObject(PetriObject petriObject) {

        if (this.petriObject != null) {
            throw new IllegalStateException("Petri object is already set. Probably you are trying to add one transition" +
                    " to two different Petri objects. Transition:\n" + this);
        }

        this.petriObject = petriObject;
    }

    public void resetPetriObject() {
        petriObject = null;
    }

    public boolean isCollectStatistics() {
        return collectStatistics;
    }

    public void validate() throws PetriObjectModelException {
        if (arcsIn.isEmpty()) {
            throw new PetriObjectModelException("Transition: " + this + " has no arcs in");
        }

        if (arcsOut.isEmpty()) {
            throw new PetriObjectModelException("Transition: " + this + " has no arcs out");
        }

        boolean hasNotInformationalArcIn = false;
        for (ArcIn arcIn : arcsIn) {
            if (!(arcIn instanceof InformationalArcIn)) {
                hasNotInformationalArcIn = true;
            }
        }
        if (!hasNotInformationalArcIn) {
            throw new PetriObjectModelException("Transition: " + this + " has informational arc in, but no not informational arc in");
        }
    }

    public String toString() {
        return "Transition{" +
                "transitionId=" + transitionId +
                ", transitionName='" + transitionName + '\'' +
                ", priority=" + priority +
                ", probability=" + probability +
                '}';
    }
}
