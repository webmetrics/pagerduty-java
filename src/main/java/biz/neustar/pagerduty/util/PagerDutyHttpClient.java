package biz.neustar.pagerduty.util;

import biz.neustar.pagerduty.util.Version;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class PagerDutyHttpClient extends DefaultHttpClient {
    private String subdomain;
    private AuthScope authScope;
    private UsernamePasswordCredentials creds;

    @Inject
    public PagerDutyHttpClient(@Named("pagerduty.subdomain") String subdomain,
                               @Named("pagerduty.username") String username,
                               @Named("pagerduty.password") String password) {
        this.subdomain = subdomain;
        authScope = new AuthScope(subdomain + ".pagerduty.com", 443);
        creds = new UsernamePasswordCredentials(username, password);
    }

    @Override
    protected CredentialsProvider createCredentialsProvider() {
        CredentialsProvider provider = super.createCredentialsProvider();
        provider.setCredentials(authScope, creds);

        return provider;
    }

    @Override
    protected HttpRequestExecutor createRequestExecutor() {
        return super.createRequestExecutor();
    }

    @Override
    protected HttpContext createHttpContext() {
        HttpContext ctx = super.createHttpContext();

        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        HttpHost host = new HttpHost(subdomain + ".pagerduty.com", 443, "https");
        authCache.put(host, basicAuth);

        // Add AuthCache to the execution context
        ctx.setAttribute(ClientContext.AUTH_CACHE, authCache);

        AuthState state = new AuthState();
        state.setAuthScheme(basicAuth);
        state.setAuthScope(authScope);
        state.setCredentials(creds);
        ctx.setAttribute(ClientContext.TARGET_AUTH_STATE, state);

        return ctx;
    }

    @Override
    protected HttpParams createHttpParams() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(params, "PagerDuty Client/" + Version.get());

        return params;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        try {
            registry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        } catch (Exception e) {
            throw new RuntimeException("Could not register SSL socket factor for Loggly", e);
        }

        ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(registry);
        int maxThreads = 4;
        connManager.setMaxTotal(maxThreads);
        connManager.setDefaultMaxPerRoute(maxThreads);

        return connManager;
    }
}
