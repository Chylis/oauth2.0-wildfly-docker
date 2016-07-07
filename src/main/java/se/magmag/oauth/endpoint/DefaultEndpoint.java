package se.magmag.oauth.endpoint;


import se.magmag.oauth.endpoint.config.RetryConfig;
import se.magmag.oauth.endpoint.parser.gson.GsonParser;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * DefaultEndpoint is a common base implementation for endpoint classes.
 */
@Slf4j
public abstract class DefaultEndpoint {

    //private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DefaultEndpoint.class.getName());

    @Inject
    private GsonParser gsonParser;

    private boolean debug;

    @Inject
    private RetryConfig retryConfig;

    protected WebTarget getTarget(String url) {
        return getTarget(url, getRetryConfig());
    }

    private WebTarget getTarget(String url, RetryConfig retryConfig) {

        Client client = ((ResteasyClientBuilder) ClientBuilder.newBuilder())
                .socketTimeout(retryConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .build();

        /*
        TODO: Make functionality equivalent
        if (isDebug()) {
            config.register(new LoggingFilter(LOGGER, true));
        }

        Client client = ClientBuilder.newClient(config);
        client.property(ClientProperties.CONNECT_TIMEOUT, retryConfig.getConnectionTimeout());
        client.property(ClientProperties.READ_TIMEOUT, retryConfig.getReadTimeout());
        */

        return client.target(url);
    }

    protected EndpointResponse invoke(final Invocation invocation) throws EndpointException {
        return invoke(invocation, getRetryConfig());
    }

    private EndpointResponse handleResponse(Response response) throws EndpointException {
        final EndpointResponse endpointResponse = new EndpointResponse();
        final String message = response.readEntity(String.class);
        endpointResponse.setMessage(message);
        endpointResponse.setStatusCode(response.getStatus());
        endpointResponse.setLocation(response.getLocation());
        return endpointResponse;
    }

    private EndpointResponse invoke(final Invocation invocation, final RetryConfig retryConfig) throws EndpointException {

        final Callable<EndpointResponse> callable = new Callable<EndpointResponse>() {
            public EndpointResponse call() throws EndpointException {
                LOG.info("Calling invokation");
                final Response response = invocation.invoke(Response.class);
                return handleResponse(response);
            }
        };

        // Don't retry if we get a RateLimitExceededException.
        final Retryer<EndpointResponse> retryer = RetryerBuilder.<EndpointResponse>newBuilder()
                .retryIfExceptionOfType(ResponseProcessingException.class)
                .retryIfExceptionOfType(ProcessingException.class)
                .retryIfExceptionOfType(WebApplicationException.class)
                .withWaitStrategy(WaitStrategies.incrementingWait(retryConfig.getWaitUntilRetryMs(), TimeUnit.MILLISECONDS,
                        retryConfig.getWaitIncrementMs(), TimeUnit.MILLISECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(retryConfig.getRetries()))
                .build();

        try {
            return retryer.call(callable);
        } catch (RetryException e) {
            // We have tried everything we can. Fail and let task retry if required.
            throw new EndpointException(String.format("Could not request resource '%s'. Giving up after '%d' retries.", "ENTER_URL_HERE", retryConfig.getRetries()), e);
        } catch (ExecutionException e) {
            throw new EndpointException(String.format("Could not request resource '%s'.", "ENTER_URL_HERE"), e);
        }
    }

    public GsonParser getGsonParser() {
        return gsonParser;
    }

    public void setGsonParser(GsonParser gsonParser) {
        this.gsonParser = gsonParser;
    }

    private boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        if (connectionTimeout != null) {
            retryConfig.setConnectionTimeout(connectionTimeout);
        }
    }

    public void setReadTimeout(Integer readTimeout) {
        if (readTimeout != null) {
            retryConfig.setReadTimeout(readTimeout);
        }
    }

    public void setRetries(Integer retries) {
        if (retries != null) {
            retryConfig.setRetries(retries);
        }
    }

    public void setWaitIncrementMs(Integer waitIncrementMs) {
        if (waitIncrementMs != null) {
            retryConfig.setWaitIncrementMs(waitIncrementMs);
        }
    }

    public void setWaitUntilRetryMs(Integer waitUntilRetryMs) {
        if (waitUntilRetryMs != null) {
            retryConfig.setWaitUntilRetryMs(waitUntilRetryMs);
        }
    }

    private RetryConfig getRetryConfig() {
        return retryConfig;
    }

    public void setRetryConfig(RetryConfig retryConfig) {
        this.retryConfig = retryConfig;
    }

}
