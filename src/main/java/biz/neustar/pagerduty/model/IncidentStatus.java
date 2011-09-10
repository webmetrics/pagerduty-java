package biz.neustar.pagerduty.model;

public enum IncidentStatus {
    TRIGGERED("triggered"), ACKNOWLEDGED("acknowledged"), RESOLVED("resolved");

    private String apiValue;

    IncidentStatus(String apiValue) {
        this.apiValue = apiValue;
    }

    public String getApiValue() {
        return apiValue;
    }
}
