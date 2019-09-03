package edu.pedorenko.petri_object_model.model;

public class PetriObjectModelException extends Exception {
    public PetriObjectModelException(String message) {
        super(message);
    }

    public PetriObjectModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
