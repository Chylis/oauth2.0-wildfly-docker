package se.magmag.oauth.endpoint;

import lombok.Data;

/**
 * EndpointParsedResponse is a generic response object that we use in endpoint method return values,
 * in order to expose both the parsed data object and the http response code.
 * Created by magnus.eriksson on 2016-06-16.
 */
@Data
public class EndpointParsedResponse<T> {
    private T data;
    private int httpResponseCode;
}
