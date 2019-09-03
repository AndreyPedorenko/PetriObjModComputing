package edu.pedorenko.petri_object_model.computing_model.petri_object.transition;

import edu.pedorenko.petri_object_model.event_protocol.state_model.TransitionBufferState;
import java.util.ArrayList;
import java.util.List;

class EventTimeBuffer {
    private List<Double> eventTimesBuffer = new ArrayList<>();

    private int eventsAmount;

    private double nearestEventTime;
    private int nearestEventTimeId;

    EventTimeBuffer() {
        nearestEventTime = Double.MAX_VALUE;
        eventTimesBuffer.add(nearestEventTime);
        nearestEventTimeId = 0;
        eventsAmount = 0;
    }

    int getEventsAmount() {
        return eventsAmount;
    }

    double getNearestEventTime() {
        return nearestEventTime;
    }

    void addEvent(double eventTime) {

        if (eventsAmount == 0) {

            eventTimesBuffer.set(0, eventTime);
            nearestEventTime = eventTime;
            nearestEventTimeId = 0;

        } else {

            eventTimesBuffer.add(eventTime);

            if (eventTime < nearestEventTime) {

                nearestEventTime = eventTime;
                nearestEventTimeId = eventTimesBuffer.size() - 1;
            }
        }

        eventsAmount++;
    }

    void removeNearestEvent() {

        if (eventTimesBuffer.size() == 1) {

            nearestEventTime = Double.MAX_VALUE;
            eventTimesBuffer.set(0, nearestEventTime);
            nearestEventTimeId = 0;
            eventsAmount = 0;

        } else {

            eventTimesBuffer.remove(nearestEventTimeId);
            eventsAmount--;
            defineNearestEventTime();
        }
    }

    private void defineNearestEventTime() {

        nearestEventTime = Double.MAX_VALUE;
        nearestEventTimeId = 0;

        for (int i = 0; i < eventTimesBuffer.size(); ++i) {

            double eventTime = eventTimesBuffer.get(i);

            if (eventTime < nearestEventTime) {
                nearestEventTime = eventTime;
                nearestEventTimeId = i;
            }
        }
    }

    TransitionBufferState toTransitionBuffetState() {
        return new TransitionBufferState(
                new ArrayList<>(eventTimesBuffer),
                eventsAmount,
                nearestEventTime,
                nearestEventTimeId);
    }
}
