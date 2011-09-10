package biz.neustar.pagerduty.model;

public class Event {
    private String serviceKey;
    private String eventType;
    private String description;
    private String incidentKey;
    private Object details;

    public Event(String serviceKey, String eventType, String description, String incidentKey, Object details) {
        this.serviceKey = serviceKey;
        this.eventType = eventType;
        this.description = description;
        this.incidentKey = incidentKey;
        this.details = details;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIncidentKey() {
        return incidentKey;
    }

    public void setIncidentKey(String incidentKey) {
        this.incidentKey = incidentKey;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}
