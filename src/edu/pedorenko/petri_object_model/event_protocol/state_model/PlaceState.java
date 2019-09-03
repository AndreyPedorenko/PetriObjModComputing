package edu.pedorenko.petri_object_model.event_protocol.state_model;

public class PlaceState {

    private long placeId;

    private String placeName;

    private int marking;

    public PlaceState(long placeId, String placeName, int marking) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.marking = marking;
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
}
