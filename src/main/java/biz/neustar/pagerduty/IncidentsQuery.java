package biz.neustar.pagerduty;

import biz.neustar.pagerduty.util.StringUtils;
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
    private SortByField sortByField;
    private SortByDirection sortByDirection;
    private String echo;
    private String[] fields;

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
        if (sortByField != null) {
            if (sortByDirection == null) {
                sortByDirection = SortByDirection.ASC;
            }
            nvps.add(new BasicNameValuePair("sort_by", sortByField.name().toLowerCase() + ":" + sortByDirection.name().toLowerCase()));
        }
        if (echo != null) {
            nvps.add(new BasicNameValuePair("echo", echo));
        }
        if (fields != null) {
            nvps.add(new BasicNameValuePair("fields", StringUtils.join(fields)));
        }

        return URLEncodedUtils.format(nvps, "UTF-8");
    }

    public IncidentsQuery withFields(String... fields) {
        this.fields = fields;
        return this;
    }

    public IncidentsQuery withEcho(String echo) {
        this.echo = echo;
        return this;
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

    public IncidentsQuery sortBy(SortByField field, SortByDirection direction) {
        this.sortByField = field;
        this.sortByDirection = direction;
        return this;
    }

    enum SortByField {
        INCIDENT_NUMBER, CREATED_ON, RESOLVED_ON
    }

    enum SortByDirection {
        ASC, DESC
    }
}
