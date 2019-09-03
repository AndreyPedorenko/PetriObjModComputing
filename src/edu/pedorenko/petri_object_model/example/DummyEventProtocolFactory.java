package edu.pedorenko.petri_object_model.example;

import edu.pedorenko.petri_object_model.event_protocol.EventProtocol;
import edu.pedorenko.petri_object_model.event_protocol.EventProtocolFactory;

public class DummyEventProtocolFactory implements EventProtocolFactory {

    public DummyEventProtocolFactory() {
    }

    public EventProtocol createEventProtocol() {
        return new DummyEventProtocol();
    }
}
