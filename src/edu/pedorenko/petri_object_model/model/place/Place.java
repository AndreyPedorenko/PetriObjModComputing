package edu.pedorenko.petri_object_model.model.place;

import edu.pedorenko.petri_object_model.model.PetriObject;

public class Place {

    private long placeId;

    private String placeName;

    private int marking;

    private PetriObject petriObject;

    private boolean collectStatistics;

    public Place(long placeId, String placeName, int marking, boolean collectStatistics) {

        if (marking < 0) {
            throw new IllegalArgumentException("Illegal argument marking = " + marking + "\n" +
                    "Place marking can't be less then 0.");
        }

        this.placeId = placeId;
        this.placeName = placeName;
        this.marking = marking;
        this.collectStatistics = collectStatistics;
    }

    public Place(long placeId, String placeName, int marking) {
        this(placeId, placeName, marking, true);
    }

    public Place(long placeId, String placeName, boolean collectStatistics) {
        this(placeId, placeName, 0, collectStatistics);
    }


    public Place(long placeId, String placeName) {
        this(placeId, placeName, 0, true);
    }

    public long getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public int getMarking() {
        return marking;
    }

    public PetriObject getPetriObject() {
        return petriObject;
    }

    public void setPetriObject(PetriObject petriObject) {

        if (this.petriObject != null) {
            throw new IllegalStateException("Petri object is already set. Probably you are trying to add one place to" +
                    " two different Petri objects. Place:\n" + this);
        }

        this.petriObject = petriObject;
    }

    public void resetPetriObject() {
        this.petriObject = null;
    }

    public boolean isCollectStatistics() {
        return collectStatistics;
    }

    public String toString() {
        return "Place{" +
                "placeId=" + placeId +
                ", placeName='" + placeName + '\'' +
                ", marking=" + marking +
                '}';
    }
}
