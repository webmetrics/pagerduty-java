package biz.neustar.pagerduty;

import biz.neustar.pagerduty.model.PagerDutyError;

public class QueryException extends Exception {
    private PagerDutyError details;

    public QueryException(PagerDutyError details) {
        super(details.getMessage());
        this.details = details;
    }

    public PagerDutyError getDetails() {
        return details;
    }
}
