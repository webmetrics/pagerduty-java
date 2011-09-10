PagerDuty Java API
==================

This is a simple Java API that wraps the [PagerDuty](http://www.pagerduty.com/) REST APIs documented here:

 - [Integration API](http://www.pagerduty.com/docs/integration-api/integration-api-documentation)
 - [Incidents Query API](http://www.pagerduty.com/docs/rest-api/incidents)
 - [Schedules API](http://www.pagerduty.com/docs/rest-api/schedules) (not yet implemented)

Downloading
-----------

The easiest way to get started is to just add this dependency in to your Maven build:

    <dependency>
        <groupId>biz.neustar</groupId>
        <artifactId>pagerduty</artifactId>
        <version>1.0-beta-1</version>
    </dependency>

If you don't use Maven, you can still find the jars in the central Maven repository. You'll also need the following dependencies:

 - [Jackson](http://jackson.codehaus.org/) 1.8.5 or later
 - [Apache HttpClient](http://hc.apache.org/httpcomponents-client-ga/) 4.1.2 or later
 - [JSR 330](http://code.google.com/p/atinject/) (aka javax.inject) for easy CI integration

Using
-----

The API is pretty straight forward if you've read the PagerDuty API docs:

    PagerDutyClient client = new PagerDutyClient("subdomain", "username", "password");

    // list incident counts
    IncidentsQuery query = new IncidentsQuery().withStatus("resolved").assignedToUser("bob");
    int count = client.getIncidentsCount(DateUtils.daysAgo(5), DateUtils.now(), query);

    // trigger a new incident
    Map details = new HashMap();
    details.put("foo", "bar");
    EventResponse response = client.trigger("service_key", "optional description", "optional incident key", details);
    String incidentKey = response.getIncidentKey();

    // resolve the incident
    response = client.resolve("service_key", "optional description", incidentKey, details);

You can also wire up the PagerDutyClient using dependency injection frameworks such as Guice. The three constructor parameters are bound to the following @Named parameters:

 - @Named("pagerduty.subdomain")
 - @Named("pagerduty.username")
 - @Named("pagerduty.password")

Note: The client *always uses SSL* to make it's HTTP requests and until we hear a good reason we won't be offering an option to change this.

License
-------

This project is licensed under the [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) license.