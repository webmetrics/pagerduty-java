package biz.neustar.pagerduty;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.Map;

/**
         "trigger_summary_data":{
            "subject":"ERROR: dal-ff queue has stalled!"
 */
public class Incident {
    private Date lastStatusChangeOn;
    private Date createdOn;
    private String triggerDetailsHtmlUrl;
    private String incidentKey;
    private long incidentNumber;
    private String htmlUrl;
    private String resolved;
    private String assignedToUser;
    private Service service;
    private User lastStatusChangeBy;
    private Map triggerSummaryData;

    public Date getLastStatusChangeOn() {
        return lastStatusChangeOn;
    }

    public void setLastStatusChangeOn(Date lastStatusChangeOn) {
        this.lastStatusChangeOn = lastStatusChangeOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getTriggerDetailsHtmlUrl() {
        return triggerDetailsHtmlUrl;
    }

    public void setTriggerDetailsHtmlUrl(String triggerDetailsHtmlUrl) {
        this.triggerDetailsHtmlUrl = triggerDetailsHtmlUrl;
    }

    public String getIncidentKey() {
        return incidentKey;
    }

    public void setIncidentKey(String incidentKey) {
        this.incidentKey = incidentKey;
    }

    public long getIncidentNumber() {
        return incidentNumber;
    }

    public void setIncidentNumber(long incidentNumber) {
        this.incidentNumber = incidentNumber;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public String getAssignedToUser() {
        return assignedToUser;
    }

    public void setAssignedToUser(String assignedToUser) {
        this.assignedToUser = assignedToUser;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getLastStatusChangeBy() {
        return lastStatusChangeBy;
    }

    public void setLastStatusChangeBy(User lastStatusChangeBy) {
        this.lastStatusChangeBy = lastStatusChangeBy;
    }

    public Map getTriggerSummaryData() {
        return triggerSummaryData;
    }

    public void setTriggerSummaryData(Map triggerSummaryData) {
        this.triggerSummaryData = triggerSummaryData;
    }
}
