package se.magmag.oauth.endpoint.parser;

import se.magmag.oauth.endpoint.EndpointParsedResponse;
import se.magmag.oauth.endpoint.EndpointResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import se.magmag.oauth.endpoint.parser.gson.GsonParser;

import javax.inject.Inject;

/**
 * Created by magnus.eriksson on 07/07/16.
 */
@Slf4j
public class EndpointResponseParser {

    @Inject
    private GsonParser gsonParser;

    public <T> EndpointParsedResponse<T> parseResponse(EndpointResponse response, Class<T> clazz) {
        return parseResponse(HttpStatus.SC_OK, response, clazz);
    }

    public <T> EndpointParsedResponse<T> parseResponse(int expectedStatusCode, EndpointResponse response, Class<T> clazz) {

        LOG.info(String.format("Response: '%s' - '%s'", response.getStatusCode(), response.getMessage()));

        final EndpointParsedResponse<T> parsedResponse = new EndpointParsedResponse<>();
        parsedResponse.setHttpResponseCode(response.getStatusCode());

        if (response.getStatusCode() == expectedStatusCode) {
            T data = gsonParser.getGson().fromJson(response.getMessage(), clazz);
            parsedResponse.setData(data);
        } else {
            LOG.warn(String.format("Failed parsing response: '%s': '%s'",
                    response.getStatusCode(),
                    response.getMessage()));
        }
        return parsedResponse;
    }
}
