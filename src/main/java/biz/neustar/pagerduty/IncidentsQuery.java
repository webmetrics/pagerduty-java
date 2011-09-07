package biz.neustar.pagerduty;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class IncidentsQuery {
    private String username;
    private Set<String> serviceIds = new HashSet<String>();
    private Set<String> statuses = new HashSet<String>();
    private Integer offset;
    private Integer limit;

    public IncidentsQuery() {
    }

    String queryParams() {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (username != null) {
            nvps.add(new BasicNameValuePair("assigned_to_user", username));
        }
        for (String serviceId : serviceIds) {
            nvps.add(new BasicNameValuePair("service", serviceId));
        }
        for (String status : statuses) {
            nvps.add(new BasicNameValuePair("status", status));
        }
        if (offset != null) {
            nvps.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        }
        if (limit != null) {
            nvps.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        }

        return URLEncodedUtils.format(nvps, "UTF-8");
    }

    public IncidentsQuery withStatus(String... statuses) {
        Collections.addAll(this.statuses, statuses);

        return this;
    }

    public IncidentsQuery assignedToUser(String username) {
        this.username = username;
        return this;
    }

    public IncidentsQuery withServices(String... serviceIds) {
        Collections.addAll(this.serviceIds, serviceIds);

        return this;
    }

    public IncidentsQuery offset(int offset) {
        this.offset = offset;

        return this;
    }

    public IncidentsQuery limit(int limit) {
        this.limit = limit;

        return this;
    }
}
