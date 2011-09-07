package biz.neustar.pagerduty;

import biz.neustar.pagerduty.util.LowerCaseWithUnderscoresStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
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
        this.mapper = mapper;
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

    public int getIncidentsCount(Date start, Date end, IncidentsQuery query) throws IOException, PagerDutyException {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO8601_FORMAT);
        HttpGet get = new HttpGet(baseUrl + "/api/v1/incidents/count?since=" + sdf.format(start) + "&until=" + sdf.format(end) + "&" + query.queryParams());
        System.out.println(get.getURI());

        HttpResponse response = client.execute(get);
        String s = EntityUtils.toString(response.getEntity());
        if (s.startsWith("{\"error\":")) {
            PagerDutyErrorWrapper wrapper = mapper.readValue(s, PagerDutyErrorWrapper.class);
            throw new PagerDutyException(wrapper.getError());
        } else {
            IncidentCount count = mapper.readValue(s, IncidentCount.class);
            return count.getTotal();
        }
    }

    public List<Incident> getIncidents(Date start, Date end, IncidentsQuery query) throws IOException, PagerDutyException {
        SimpleDateFormat sdf = new SimpleDateFormat(ISO8601_FORMAT);
        HttpGet get = new HttpGet(baseUrl + "/api/v1/incidents?since=" + sdf.format(start) + "&until=" + sdf.format(end) + "&" + query.queryParams());
        System.out.println(get.getURI());

        HttpResponse response = client.execute(get);
        String s = EntityUtils.toString(response.getEntity());
        if (s.startsWith("{\"error\":")) {
            PagerDutyErrorWrapper wrapper = mapper.readValue(s, PagerDutyErrorWrapper.class);
            throw new PagerDutyException(wrapper.getError());
        } else {
            IncidentResults results = mapper.readValue(s, IncidentResults.class);
            return results.getIncidents();
        }
    }
}
