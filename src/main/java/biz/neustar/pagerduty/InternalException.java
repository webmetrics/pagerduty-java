package biz.neustar.pagerduty;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class InternalException extends Exception {
    private int statusCode;
    private String reason;
    private String body;

    public InternalException(HttpResponse response) {
        super(response.getStatusLine().toString());

        statusCode = response.getStatusLine().getStatusCode();
        reason = response.getStatusLine().getReasonPhrase();

        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            // ignore!
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReason() {
        return reason;
    }

    public String getBody() {
        return body;
    }
}
