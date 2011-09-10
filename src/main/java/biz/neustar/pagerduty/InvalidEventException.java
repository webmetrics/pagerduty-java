package biz.neustar.pagerduty;

import biz.neustar.pagerduty.model.EventResponse;

public class InvalidEventException extends Exception {
    private EventResponse eventResponse;

    public InvalidEventException(EventResponse eventResponse) {
        super(eventResponse.getMessage());
        this.eventResponse = eventResponse;
    }

    public EventResponse getEventResponse() {
        return eventResponse;
    }
}
