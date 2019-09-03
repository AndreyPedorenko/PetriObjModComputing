package edu.pedorenko.petri_object_model.computing_model.petri_object.place;

import edu.pedorenko.petri_object_model.event_protocol.state_model.PlaceState;

public class ComputingPlace {

    private long placeId;

    private String placeName;

    private int marking;

    public ComputingPlace(long placeId, String placeName, int initialMarking) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.marking = initialMarking;
    }

    public synchronized void increaseMarking(int delta) {

        if (delta <= 0) {
            throw new IllegalArgumentException("Illegal argument delta = " + delta + "\n" +
                    "It is only possible to increase marking by delta more then 0.");
        }

        marking += delta;
    }

    public synchronized void decreaseMarking(int delta) throws MarkingException {

        if (delta <= 0) {
            throw new IllegalArgumentException("Illegal argument delta = " + delta + "\n" +
                    "It is only possible to decrease marking by delta more then 0.");
        }

        if (delta > marking) {
            throw new MarkingException("Can't decrease marking = " + marking + " by delta = " + delta + ".");
        }

        marking -= delta;
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

    public PlaceState toPlaceState() {
        return new PlaceState(placeId, placeName, marking);
    }
}
