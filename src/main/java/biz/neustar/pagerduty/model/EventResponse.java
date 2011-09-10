package biz.neustar.pagerduty.model;

import java.util.List;

public class EventResponse {
    private String status;
    private String incidentKey;
    private String message;
    private List<String> warnings;
    private List<String> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIncidentKey() {
        return incidentKey;
    }

    public void setIncidentKey(String incidentKey) {
        this.incidentKey = incidentKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
