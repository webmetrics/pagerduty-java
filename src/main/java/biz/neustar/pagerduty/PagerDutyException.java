package biz.neustar.pagerduty;

public class PagerDutyException extends Exception {
    private PagerDutyError details;

    public PagerDutyException(PagerDutyError details) {
        super(details.getMessage());
        this.details = details;
    }

    public PagerDutyError getDetails() {
        return details;
    }
}
