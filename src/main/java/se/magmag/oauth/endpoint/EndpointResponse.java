package se.magmag.oauth.endpoint;

import lombok.Data;

import java.net.URI;

/**
 * EndpointResponse contains the response from an endpoint with unparsed data.
 */
@Data
public class EndpointResponse {
    private int statusCode;
    private String message;
    private URI location;
}
