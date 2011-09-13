package biz.neustar.pagerduty;

import biz.neustar.pagerduty.model.*;
import biz.neustar.pagerduty.util.LowerCaseWithUnderscoresStrategy;
import biz.neustar.pagerduty.util.PagerDutyHttpClient;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PagerDutyClient {
    private static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private PagerDutyHttpClient client;

    private String baseUrl;
    private ObjectMapper mapper;

    @Inject
    public PagerDutyClient(@Named("pagerduty.subdomain") String subdomain,
                           @Named("pagerduty.username") String username,
                           @Named("pagerduty.password") String password) {
        this.client = new PagerDutyHttpClient(subdomain, username, password);
        this.baseUrl = "https://" + subdomain + ".pagerduty.com";
        mapper = makeObjectMapper();
    }

    private static ObjectMapper makeObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Use ISO dates
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        // ignore unexpected fields
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy());

        return mapper;
    }

    /**
     * Returns the count of incidents based on the supplied date range and query parameters.
     *
     * @param start the start of the query range
     * @param end the end of the query range
     * @param query the query details, such as new IncidentsQuery().sortBy(RESOLVED_ON, ASC).withStatus("resolved")
     * @return the count of incidents
     * @throws IOException when there is an IO problem such as making a network request
     * @throws QueryException when PagerDuty rejects the query being made and responds with an error and reason
     * @throws InternalException when PagerDuty returns a 5xx error code
     */
    public int getIncidentsCount(Date start, Date end, IncidentsQuery query) throws IOException, QueryException, InternalException {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO8601_FORMAT);
        HttpGet get = new HttpGet(baseUrl + "/api/v1/incidents/count?since=" + sdf.format(start) + "&until=" + sdf.format(end) + "&" + query.queryParams());

        HttpResponse response = client.execute(get);
        try {
            InputStream is = response.getEntity().getContent();
            int sc = response.getStatusLine().getStatusCode();
            if (sc >= 500) {
                throw new InternalException(response);
            } else if (sc >= 400) {
                PagerDutyErrorWrapper wrapper = mapper.readValue(is, PagerDutyErrorWrapper.class);
                throw new QueryException(wrapper.getError());
            } else {
                IncidentCount count = mapper.readValue(is, IncidentCount.class);
                return count.getTotal();
            }
        } finally {
            EntityUtils.consume(response.getEntity());
        }
    }

    /**
     * Returns the incidents based on the supplied date range and query parameters.
     *
     * @param start the start of the query range
     * @param end the end of the query range
     * @param query the query details, such as new IncidentsQuery().sortBy(RESOLVED_ON, ASC).withStatus("resolved")
     * @return the count of incidents
     * @throws IOException when there is an IO problem such as making a network request
     * @throws QueryException when PagerDuty rejects the query being made and responds with an error and reason
     * @throws InternalException when PagerDuty returns a 5xx error code
     */
    public List<Incident> getIncidents(Date start, Date end, IncidentsQuery query) throws IOException, QueryException, InternalException {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO8601_FORMAT);
        HttpGet get = new HttpGet(baseUrl + "/api/v1/incidents?since=" + sdf.format(start) + "&until=" + sdf.format(end) + "&" + query.queryParams());

        HttpResponse response = client.execute(get);
        try {
            InputStream is = response.getEntity().getContent();
            int sc = response.getStatusLine().getStatusCode();
            if (sc >= 500) {
                throw new InternalException(response);
            } else if (sc >= 400) {
                PagerDutyErrorWrapper wrapper = mapper.readValue(is, PagerDutyErrorWrapper.class);
                throw new QueryException(wrapper.getError());
            } else {
                IncidentResults results = mapper.readValue(is, IncidentResults.class);
                return results.getIncidents();
            }
        } finally {
            EntityUtils.consume(response.getEntity());
        }
    }

    /**
     * Triggers a new incident in PagerDuty for the supplied service, including an optional description, incident key,
     * and details to go along with the message.
     *
     * @param serviceKey the service key to for the incident
     * @param description an optional description for the incident
     * @param incidentKey an optional key (if not supplied, PagerDuty will generate one and return it)
     * @param details an object that will be converted to a JSON structure and included with the incident details (can be Map, real object, etc)
     * @return the event response details
     * @throws IOException when there is an IO problem such as making a network request
     * @throws InternalException when a 5xx response code is returned
     * @throws InvalidEventException when a 400 response code is returned, indicating that the event supplied is invalid
     */
    public EventResponse trigger(String serviceKey, String description, String incidentKey, Object details) throws IOException, InternalException, InvalidEventException {
        return event("trigger", serviceKey, description, incidentKey, details);
    }

    /**
     * Acknowledges an existing event in PagerDuty for the supplied service and incident key, including an optional
     * description and details to go along with the message.
     *
     * @param serviceKey the service key to for the incident
     * @param description an optional description for the incident
     * @param incidentKey a required incident key that maps to the incident you wish to acknowledge
     * @param details an object that will be converted to a JSON structure and included with the incident details (can be Map, real object, etc)
     * @return the event response details
     * @throws IOException when there is an IO problem such as making a network request
     * @throws InternalException when a 5xx response code is returned
     * @throws InvalidEventException when a 400 response code is returned, indicating that the event supplied is invalid
     */
    public EventResponse acknowledge(String serviceKey, String description, String incidentKey, Object details) throws IOException, InternalException, InvalidEventException {
        return event("acknowledge", serviceKey, description, incidentKey, details);
    }

    /**
     * Resolves an existing event in PagerDuty for the supplied service and incident key, including an optional
     * description and details to go along with the message.
     *
     * @param serviceKey the service key to for the incident
     * @param description an optional description for the incident
     * @param incidentKey a required incident key that maps to the incident you wish to acknowledge
     * @param details an object that will be converted to a JSON structure and included with the incident details (can be Map, real object, etc)
     * @return the event response details
     * @throws IOException when there is an IO problem such as making a network request
     * @throws InternalException when a 5xx response code is returned
     * @throws InvalidEventException when a 400 response code is returned, indicating that the event supplied is invalid
     */
    public EventResponse resolve(String serviceKey, String description, String incidentKey, Object details) throws IOException, InternalException, InvalidEventException {
        return event("resolve", serviceKey, description, incidentKey, details);
    }

    private EventResponse event(String type, String serviceKey, String description, String incidentKey, Object details) throws IOException, InternalException, InvalidEventException {
        HttpPost post = new HttpPost("https://events.pagerduty.com/generic/2010-04-15/create_event.json");

        ByteOutputStream baos = new ByteOutputStream();
        mapper.writeValue(baos, new Event(serviceKey, type, description, incidentKey, details));
        post.setEntity(new ByteArrayEntity(baos.getBytes()));

        HttpResponse response = client.execute(post);
        try {
            int sc = response.getStatusLine().getStatusCode();
            InputStream is = response.getEntity().getContent();
            if (sc == 200) {
                return mapper.readValue(is, EventResponse.class);
            } else if (sc == 400) {
                EventResponse resp = mapper.readValue(is, EventResponse.class);
                throw new InvalidEventException(resp);
            } else {
                throw new InternalException(response);
            }
        } finally {
            EntityUtils.consume(response.getEntity());
        }
    }
}
