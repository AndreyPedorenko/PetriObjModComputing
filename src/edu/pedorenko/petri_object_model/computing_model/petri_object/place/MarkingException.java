package edu.pedorenko.petri_object_model.computing_model.petri_object.place;

public class MarkingException extends Exception {
    public MarkingException(String message) {
        super(message);
    }

    public MarkingException(String message, Throwable cause) {
        super(message, cause);
    }
}
